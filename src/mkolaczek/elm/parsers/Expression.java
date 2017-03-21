package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.psi.Tokens;

import static com.google.common.collect.Sets.newHashSet;
import static mkolaczek.elm.parsers.Basic.spacePrefix;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.psi.Tokens.*;

public class Expression {
    public static Parser definition() {
        //TODO: continue
        Parser valueDefinition =
                sequence(
                        or(
                                expect(Tokens.LOW_VAR),
                                Basic.operator().ll2(newHashSet(LPAREN), newHashSet(RUNE_OF_AUTOCOMPLETION, SYM_OP))
                        ),
                        or(
                                sequence(
                                        expect(Tokens.COLON),
                                        Type.expression
                                ),
                                definitionEnd()
                        )
                );


        //TODO: continue
        return or(
                valueDefinition,
                sequence(
                        Pattern.term(),
                        definitionEnd()
                )
        );
    }

    private static Parser definitionEnd() {
        return sequence(
                spacePrefix(Pattern.term()),
                maybeWhitespace(expect(EQUALS)),
                expression()
        );
    }

    private static Parser expression() {
        //TODO: implement
        return or(
                let()
        );
    }

    private static Parser let() {
        //TODO: implement
        return sequence(
                maybeWhitespace(expect(Tokens.LET))
        );
    }
}
