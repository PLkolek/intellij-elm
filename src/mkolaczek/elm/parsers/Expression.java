package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static com.google.common.collect.Sets.newHashSet;
import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.Literal.glsl;
import static mkolaczek.elm.parsers.Literal.literal;
import static mkolaczek.elm.parsers.core.DottedVar.dottedVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.indentedMany1;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;
import static mkolaczek.elm.parsers.core.WhiteSpace.noWhiteSpace;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class Expression {

    public static Parser definition() {
        return or("definition",
                operatorDefinition(),
                valueDefinition()
        );
    }

    @NotNull
    public static Parser valueDefinition() {
        return or(
                sequence(
                        expect(Tokens.LOW_VAR).as(Elements.VALUE_NAME_REF).as(MAIN_DEFINED_VALUES),
                        expect(Tokens.COLON),
                        Type.expression
                ).as(Elements.TYPE_ANNOTATION).ll2(newHashSet(LOW_VAR), newHashSet(COLON)),
                sequence(
                        sequence(
                                expect(Tokens.LOW_VAR).as(VALUE_NAME).as(MAIN_DEFINED_VALUES),
                                spacePrefix(definedValues())
                        ).as(DEFINED_VALUES),
                        definitionEnd()
                ),
                sequence(
                        sequence(
                                Pattern.term().as(MAIN_DEFINED_VALUES),
                                spacePrefix(definedValues())
                        ).as(DEFINED_VALUES),
                        definitionEnd()
                )
        );
    }

    @NotNull
    public static Parser operatorDefinition() {
        return sequence(
                Basic.operator(Elements.OPERATOR_SYMBOL)
                     .ll2(newHashSet(LPAREN), newHashSet(RUNE_OF_AUTOCOMPLETION, SYM_OP, CONS)),
                or(
                        sequence(
                                expect(Tokens.COLON),
                                Type.expression
                        ).swapAs(TYPE_ANNOTATION),
                        sequence(
                                spacePrefix(definedValues()),
                                definitionEnd()
                        ).as(DEFINED_VALUES)
                )
        ).as(Elements.OPERATOR_DECLARATION);
    }

    private static Parser definitionEnd() {
        return sequence("definitionEnd",
                maybeWhitespace(expect(EQUALS)),
                maybeWhitespace(expression)
        );
    }

    private static Parser definedValues() {
        return Pattern.term();
    }

    private static final ParserBox expression = new ParserBox("expression");

    static {
        expression.setParser(
                or("expression",
                        finalExpression(),
                        termOperation()
                ).as(EXPRESSION)
        );
    }

    @NotNull
    private static Parser finalExpression() {
        return or(
                let(),
                if_(),
                case_(),
                function()
        );
    }

    private static Parser termOperation() {
        return sequence("term operation",
                possiblyNegativeTerm(),
                spacePrefix(
                        or(
                                term(),
                                binaryOperator()
                        )
                )
        );
    }

    private static Parser binaryOperator() {
        return or("infix binary operator use",
                minusOperator(),
                otherOperator()
        );
    }

    @NotNull
    private static Sequence minusOperator() {
        return sequence(
                expect(MINUS).as(Elements.OPERATOR_SYMBOL_REF),
                maybeWhitespace(
                        or(
                                term(),
                                finalExpression().as(EXPRESSION)
                        ).as(Elements.OPERAND)
                )
        );
    }

    @NotNull
    private static Sequence otherOperator() {
        return sequence(
                expect(SYM_OP).as(Elements.OPERATOR_SYMBOL_REF),
                maybeWhitespace(or(
                        possiblyNegativeTerm(),
                        finalExpression().as(EXPRESSION)
                ).as(Elements.OPERAND))
        );
    }

    private static Parser possiblyNegativeTerm() {
        return or(
                sequence(
                        expect(MINUS),
                        noWhiteSpace(term())
                ),
                term()
        );
    }

    private static Parser term() {
        return or(
                variable(),
                literal(),
                glsl(),
                list("list expression", expression),
                accessible(record("record expression", fieldSuffix())),
                accessible(tupleLike()),
                accessor()
        ).as(Elements.TERM);
    }

    private static Parser accessor() {
        return sequence("accessor",
                expect(DOT),
                noWhiteSpace(expect(LOW_VAR))
        );
    }

    private static Parser tupleLike() {
        return or(
                operator(),
                Basic.tuple("tuple expression", expression)
        );
    }

    private static Parser operator() {
        return or(
                Basic.operator(Elements.OPERATOR_SYMBOL_REF).ll2(newHashSet(LPAREN), newHashSet(SYM_OP, CONS)),
                Basic.parens("minus operator", expect(MINUS)).ll2(newHashSet(LPAREN), newHashSet(MINUS)),
                Basic.parens("tuple operator", or(expect(Tokens.COMMA_OP), expect(COMMA)))
                     .ll2(newHashSet(LPAREN), newHashSet(COMMA, COMMA_OP))
        );
    }

    @NotNull
    private static Sequence fieldSuffix() {
        return sequence("record field suffix",
                maybeWhitespace(expect(Tokens.EQUALS)),
                maybeWhitespace(expression)
        );
    }

    @NotNull
    private static Sequence variable() {
        return accessible(dottedVar("qualified variable",
                Elements.MODULE_NAME_REF,
                Elements.VAR).as(Elements.QUALIFIED_VAR));
    }

    @NotNull
    private static Sequence accessible(Parser parser) {
        return sequence(
                parser,
                many("accessors",
                        noWhiteSpace(expect(DOT)),
                        noWhiteSpace(expect(LOW_VAR))
                )
        );
    }

    private static Parser function() {
        return sequence(
                expect(LAMBDA),
                maybeWhitespace(definedValues()).as(DEFINED_VALUES),
                spacePrefix(definedValues()).as(DEFINED_VALUES),
                maybeWhitespace(expect(ARROW)),
                maybeWhitespace(expression)
        ).as(Elements.LAMBDA_EXPRESSION);
    }

    private static Parser case_() {
        return sequence(
                expect(CASE),
                maybeWhitespace(expression),
                expect(OF),
                indentedMany1(caseBranch())
        ).as(CASE_EXPRESSION);
    }

    private static Parser caseBranch() {
        return sequence(
                Pattern.expression.as(DEFINED_VALUES),
                expect(Tokens.ARROW),
                maybeWhitespace(expression)
        ).as(Elements.CASE_BRANCH);
    }

    private static Parser if_() {
        return sequence(
                maybeWhitespace(expect(Tokens.IF)),
                maybeWhitespace(expression),
                maybeWhitespace(expect(Tokens.THEN)),
                maybeWhitespace(expression),
                maybeWhitespace(expect(Tokens.ELSE)),
                maybeWhitespace(expression) //this should handle else if
        ).as(IF_EXPRESSION);

    }

    private static Parser let() {
        return sequence(
                maybeWhitespace(expect(Tokens.LET)),
                Many.indentedMany1(definition()),
                maybeWhitespace(expect(Tokens.IN)),
                maybeWhitespace(expression)
        ).as(Elements.LET_EXPRESSION);
    }

}
