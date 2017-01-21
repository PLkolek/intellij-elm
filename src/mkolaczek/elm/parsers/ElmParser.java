package mkolaczek.elm.parsers;

import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.parsers.faultTolerant.FTDeclaration;
import mkolaczek.elm.parsers.faultTolerant.FTModule;
import mkolaczek.elm.parsers.faultTolerant.FTParser;
import mkolaczek.elm.parsers.faultTolerant.Sequence;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;

public class ElmParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        Marker rootMarker = builder.mark();
        PsiBuilder.Marker module = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        program(builder);
        if (!builder.eof()) {
            builder.error("EOF expected");
        }
        consumeRest(builder);
        if (startingOffset != builder.getCurrentOffset()) {
            module.done(Elements.MODULE_NODE);
        } else {
            module.drop();
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void program(@NotNull PsiBuilder builder) {
        FTParser parser = Sequence.rootSequence("program",
                FTModule.moduleHeader(),
                FTDeclaration.declarations()
        );
        parser.computeNextTokens(Sets.newHashSet());
        parser.parse(builder);
    }



    private void consumeRest(PsiBuilder builder) {
        while (!builder.eof()) {
            builder.advanceLexer();
        }
    }
}
