package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;

public class Expression {
    public static Parser definition() {
        //TODO: continue
        Parser valueDefinition =
                sequence(
                        or(
                                expect(Tokens.LOW_VAR),
                                Basic.operator().ll2()
                        ),
                        or(
                                sequence(
                                        expect(Tokens.COLON),
                                        Type.expression
                                )
                        )
                );


        //TODO: continue
        return or(
                valueDefinition,
                Pattern.term()
        );
    }
}
