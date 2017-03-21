package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static com.google.common.collect.Sets.newHashSet;
import static mkolaczek.elm.parsers.Basic.spacePrefix;
import static mkolaczek.elm.parsers.Literal.glsl;
import static mkolaczek.elm.parsers.Literal.literal;
import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.DottedVar.dottedVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.indentedMany1;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.parsers.core.WhiteSpace.noWhiteSpace;
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
                        somethingSomething()
                )
        );
    }

    private static Parser somethingSomething() {
        //TODO: obviously...
        return sequence(
                possiblyNegativeTerm()
        );
    }

    private static Parser possiblyNegativeTerm() {
        return sequence(
                tryP(expect(Tokens.MINUS)),
                term()
        );
    }

    private static Parser term() {
        return or(
                variable(),
                literal(),
                glsl(),
                list(),
                //TODO: just for testing
                expect(CAP_VAR)
        );
    }

    private static Parser list() {
        return Basic.squareBrackets("list expression",
                tryCommaSep(expression)
        );
    }

    @NotNull
    private static Sequence variable() {
        return sequence(
                dottedVar("qualified variable"),
                many("accessors",
                        noWhiteSpace(expect(DOT)),
                        noWhiteSpace(expect(LOW_VAR))
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
