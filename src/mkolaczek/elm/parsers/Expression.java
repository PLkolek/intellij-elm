package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.psi.Tokens;

import static com.google.common.collect.Sets.newHashSet;
import static mkolaczek.elm.parsers.Basic.spacePrefix;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.IndentedBlock.indentedBlock;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.indented;
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
                expression
        );
    }

    private static final ParserBox expression = new ParserBox("expression");

    static {
        //TODO: implement
        expression.setParser(
                or(
                        let(),

                        //TODO: just for testing
                        expect(CAP_VAR)
                )
        );
    }

    private static Parser let() {
        return sequence(
                maybeWhitespace(expect(Tokens.LET)),
                indentedBlock(
                        sequence(
                                definition(),
                                many(indented(definition()))
                        )
                ),
                maybeWhitespace(expect(Tokens.IN)),
                maybeWhitespace(expression)
        );
    }
}
