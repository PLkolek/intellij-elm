package mkolaczek.elm.parsers;

import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.core.Try.tryP;

public class ElmParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        Marker rootMarker = builder.mark();
        PsiBuilder.Marker module = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        program(builder);
        if (!builder.eof()) {
            throw new IllegalStateException("Parser should have consumed everything!");
        }
        if (startingOffset != builder.getCurrentOffset()) {
            module.done(Elements.MODULE_NODE);
        } else {
            module.drop();
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void program(@NotNull PsiBuilder builder) {
        Sequence parser = Sequence.sequence(
                tryP(Module.moduleHeader()),
                Declaration.declarations()
        );
        parser.parse2(builder, Sets.newHashSet());
    }
}
