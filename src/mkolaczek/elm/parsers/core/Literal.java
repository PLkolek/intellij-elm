package mkolaczek.elm.parsers.core;

import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.psi.Tokens.*;

public class Literal {
    public static Parser literal() {
        //TODO: continue
        return or(
                string()
        );
    }

    private static Parser string() {
        return or(
                multilineString(),
                singlelineString()
        );
    }

    private static Sequence multilineString() {
        return sequence(
                expect(MULTILINE_STRING),
                stringContent(),
                expect(MULTILINE_STRING)
        );
    }

    private static Parser singlelineString() {
        return sequence(
                expect(Tokens.QUOTE),
                stringContent(),
                or(expect(Tokens.QUOTE), expect(INVALID_EOL_IN_STRING))
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
