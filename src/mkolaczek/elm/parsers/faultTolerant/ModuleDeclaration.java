package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.FRESH_LINE;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.MAYBE;

public class ModuleDeclaration extends FTParserAbstr {

    private final Sequence sequence;

    public ModuleDeclaration() {
        super("Module declaration",
                ImmutableSet.of(Tokens.MODULE, Tokens.PORT, Tokens.EFFECT),
                true, Elements.MODULE_HEADER);
        this.sequence =
                new Sequence(name,
                        new Or("Module declaration keywords",
                                new Sequence("Effect module declaration keywords",
                                        expect(Tokens.EFFECT),
                                        new WhiteSpace(MAYBE),
                                        expect(Tokens.MODULE)
                                ),
                                new Sequence("Port module declaration keywords",
                                        expect(Tokens.EFFECT),
                                        new WhiteSpace(MAYBE),
                                        expect(Tokens.MODULE)
                                ),
                                expect(Tokens.MODULE)
                        ),
                        new WhiteSpace(MAYBE),
                        FTParsers.dottedCapVar("Module name", Elements.MODULE_NAME),
                        new WhiteSpace(MAYBE),
                        new Exposing(),
                        new WhiteSpace(FRESH_LINE)
                );
        sequence.computeNextTokens(Sets.newHashSet(Tokens.BEGIN_DOC_COMMENT, Tokens.IMPORT));
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        sequence.parse(builder);
      /*  Marker marker = builder.mark();
        boolean isEffectModule = builder.getTokenType() == Tokens.EFFECT;
        new Sequence(name, nextTokens,
                new Or("Module declaration keywords", )
                Combinators.or(
                        expect(Tokens.EFFECT, Tokens.MODULE),
                        expect(Tokens.PORT, Tokens.MODULE),
                        expect(Tokens.MODULE)),
                Whitespace::maybeWhitespace,
                Combinators.skipUntilFL(Basic.dottedCapVar(Elements.MODULE_NAME)),
                Whitespace::maybeWhitespace,
                isEffectModule ? Combinators.skipUntilFL(settings(), Tokens.EXPOSING) : empty(),
                Whitespace::maybeWhitespace,
                exposing(),
                Whitespace::freshLine
        );
        marker.done(Elements.MODULE_HEADER);
        return true;*/
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        sequence.computeNextTokens(myNextTokens);
    }
}
