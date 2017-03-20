package mkolaczek.elm.parsers.core;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.psi.Tokens.*;

public class Literal {
    public static Parser literal() {
        //TODO: continue
        return or(
                string(),
                character()
        );
    }

    private static Parser character() {
        return singleline(Tokens.SINGLE_QUOTE).as(Elements.CHARACTER_LITERAL);
    }

    private static Parser string() {
        return or(
                multilineString(),
                singlelineString()
        ).as(Elements.STRING_LITERAL);
    }

    private static Sequence multilineString() {
        return sequence(
                expect(MULTILINE_STRING),
                stringContent(),
                expect(MULTILINE_STRING)
        );
    }

    private static Parser singlelineString() {
        return singleline(Tokens.QUOTE);
    }

    @NotNull
    private static Sequence singleline(Token quote) {
        return sequence(
                expect(quote),
                stringContent(),
                or(expect(quote), expect(INVALID_EOL_IN_STRING))
        );
    }

    private static Parser stringContent() {
        return many("string content",
                or(
                        expect(STRING_CONTENT),
                        expect(VALID_STRING_ESCAPE_TOKEN),
                        expect(INVALID_CHARACTER_ESCAPE_TOKEN),
                        expect(INVALID_UNICODE_ESCAPE_TOKEN)
                )
        );
    }

}
