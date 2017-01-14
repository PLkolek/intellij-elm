package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.FTBasic.docComment;
import static mkolaczek.elm.parsers.faultTolerant.FTModule.moduleDeclaration;
import static mkolaczek.elm.parsers.faultTolerant.Many.manyAs;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Try.tryP;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.freshLine;

public class ModuleDeclaration extends FTParserAbstr {

    private final FTParser myParser;

    public ModuleDeclaration() {
        super("Module declaration",
                ImmutableSet.of(Tokens.MODULE, Tokens.PORT, Tokens.EFFECT, Tokens.IMPORT, Tokens.BEGIN_DOC_COMMENT),
                true, null);
        this.myParser =
                sequence("Module Header",
                        freshLine(),
                        moduleDeclaration(),
                        tryP(sequence("Doc comment", docComment(), freshLine())),
                        manyAs(Elements.IMPORTS, FTModule.importLine())
                );
        myParser.computeNextTokens(Sets.newHashSet());
    }


    @Override
    protected void parse2(PsiBuilder builder) {
        myParser.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        myParser.computeNextTokens(myNextTokens);
    }
}
