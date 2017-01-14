package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.dottedCapVar;
import static mkolaczek.elm.parsers.faultTolerant.FTModule.exposing;
import static mkolaczek.elm.parsers.faultTolerant.Or.or;
import static mkolaczek.elm.parsers.faultTolerant.Or.orAs;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Try.tryP;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.freshLine;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;

public class ModuleDeclaration extends FTParserAbstr {

    private final FTParser myParser;

    public ModuleDeclaration() {
        super("Module declaration",
                ImmutableSet.of(Tokens.MODULE, Tokens.PORT, Tokens.EFFECT, Tokens.IMPORT),
                true, null);
        this.myParser =
                sequence("Module Header",
                        moduleDeclaration(),
                        Many.manyAs(Elements.IMPORTS, FTModule.importLine())
                );
        myParser.computeNextTokens(Sets.newHashSet());
    }

    private static FTParser moduleDeclaration() {
        Sequence effectSequence =

                sequence("Module declaration",
                        expect(Tokens.EFFECT),
                        maybeWhitespace(),
                        expect(Tokens.MODULE),
                        maybeWhitespace(),
                        dottedCapVar(Elements.MODULE_NAME),
                        maybeWhitespace(),
                        FTModule.settings(),
                        maybeWhitespace(),
                        exposing(),
                        freshLine()
                );

        Sequence sequence =
                sequence("Module declaration",
                        or("Module declaration keywords",
                                sequence("Port module declaration keywords",
                                        expect(Tokens.PORT),
                                        maybeWhitespace(),
                                        expect(Tokens.MODULE)
                                ),
                                expect(Tokens.MODULE)
                        ),
                        maybeWhitespace(),
                        dottedCapVar(Elements.MODULE_NAME),
                        maybeWhitespace(),
                        exposing(),
                        freshLine()
                );
        return tryP(orAs(Elements.MODULE_HEADER, effectSequence, sequence));
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
