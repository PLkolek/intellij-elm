package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.psi.Tokens;

import static com.google.common.collect.Sets.newHashSet;
import static mkolaczek.elm.parsers.Basic.spacePrefix;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.indentedMany1;
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
                expression
        );
    }

    private static final ParserBox expression = new ParserBox("expression");

    static {
        //TODO: implement
        expression.setParser(
                or(
                        let(),
                        if_(),
                        case_(),
                        function(),
                        //TODO: just for testing
                        expect(CAP_VAR)
                )
        );
    }

    private static Parser function() {
        return sequence(
                expect(LAMBDA),
                maybeWhitespace(Pattern.term()),
                spacePrefix(Pattern.term()),
                expect(ARROW),
                expression
        );
    }

    private static Parser case_() {
        return sequence(
                expect(CASE),
                maybeWhitespace(expression),
                expect(OF),
                indentedMany1(caseBranch())
        );
    }

    private static Parser caseBranch() {
        return sequence(
                Pattern.expression,
                expect(Tokens.ARROW),
                maybeWhitespace(expression)
        );
    }

    private static Parser if_() {
        return sequence(
                maybeWhitespace(expect(Tokens.IF)),
                maybeWhitespace(expression),
                expect(Tokens.THEN),
                maybeWhitespace(expression),
                expect(Tokens.ELSE),
                maybeWhitespace(expression) //this should handle else if
        );
    }

    private static Parser let() {
        return sequence(
                maybeWhitespace(expect(Tokens.LET)),
                Many.indentedMany1(definition()),
                maybeWhitespace(expect(Tokens.IN)),
                maybeWhitespace(expression)
        );
    }

}
