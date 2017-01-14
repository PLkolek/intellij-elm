package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.FTModule.exposing;
import static mkolaczek.elm.parsers.faultTolerant.Or.or;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.freshLine;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;

public class ModuleDeclaration extends FTParserAbstr {

    private final Or or;

    public ModuleDeclaration() {
        super("Module declaration",
                ImmutableSet.of(Tokens.MODULE, Tokens.PORT, Tokens.EFFECT),
                true, Elements.MODULE_HEADER);
        Sequence effectSequence =
                sequence(name,
                        expect(Tokens.EFFECT),
                        maybeWhitespace(),
                        expect(Tokens.MODULE),
                        maybeWhitespace(),
                        FTBasic.dottedCapVar("Module name", Elements.MODULE_NAME),
                        maybeWhitespace(),
                        FTModule.settings(),
                        maybeWhitespace(),
                        exposing(),
                        freshLine()
                );

        Sequence sequence =
                sequence(name,
                        or("Module declaration keywords",
                                sequence("Effect module declaration keywords",
                                        expect(Tokens.EFFECT),
                                        maybeWhitespace(),
                                        expect(Tokens.MODULE)
                                ),
                                sequence("Port module declaration keywords",
                                        expect(Tokens.EFFECT),
                                        maybeWhitespace(),
                                        expect(Tokens.MODULE)
                                ),
                                expect(Tokens.MODULE)
                        ),
                        maybeWhitespace(),
                        FTBasic.dottedCapVar("Module name", Elements.MODULE_NAME),
                        maybeWhitespace(),
                        exposing(),
                        freshLine()
                );
        this.or = or("Module declaration", effectSequence, sequence);

        or.computeNextTokens(Sets.newHashSet(Tokens.BEGIN_DOC_COMMENT, Tokens.IMPORT));
    }


    @Override
    protected void parse2(PsiBuilder builder) {
        or.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        or.computeNextTokens(myNextTokens);
    }
}
