package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.Literal.glsl;
import static mkolaczek.elm.parsers.Literal.literal;
import static mkolaczek.elm.parsers.core.DottedVar.qualifiedVar;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Many.indentedMany1;
import static mkolaczek.elm.parsers.core.Many.many;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

class Expression {

    static Parser definition() {
        return or("definition",
                operatorDefinition(),
                valueDefinition()
        );
    }

    @NotNull
    public static Parser valueDefinition() {
        return or(
                sequence(
                        expect(Tokens.LOW_VAR).as(Elements.VALUE_NAME_REF),
                        Type.annotationEnd()
                ).as(Elements.TYPE_ANNOTATION).llk(2),
                sequence(
                        sequence(
                                or(
                                        expect(Tokens.LOW_VAR).as(VALUE_NAME).as(MAIN_DEFINED_VALUES),
                                        Pattern.term().as(MAIN_DEFINED_VALUES)
                                ),
                                spacePrefix(definedValues())
                        ).as(DEFINED_VALUES),
                        definitionEnd()
                ).as(VALUE_DECLARATION)
        );
    }

    @NotNull
    public static Parser operatorDefinition() {
        return sequence(
                Basic.operator(Elements.OPERATOR_SYMBOL)
                     .llk(2),
                or(
                        Type.annotationEnd().swapAs(TYPE_ANNOTATION),
                        sequence(
                                spacePrefix(definedValues()),
                                definitionEnd()
                        ).as(DEFINED_VALUES)
                )
        ).as(Elements.OPERATOR_DECLARATION);
    }

    private static Parser definitionEnd() {
        return sequence("definitionEnd",
                expect(EQUALS),
                expression
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
                or(
                        term(),
                        finalExpression().as(EXPRESSION)
                ).as(Elements.OPERAND)
        );
    }

    @NotNull
    private static Sequence otherOperator() {
        return sequence(
                operatorSymbol(Elements.OPERATOR_SYMBOL_REF),
                or(
                        possiblyNegativeTerm(),
                        finalExpression().as(EXPRESSION)
                ).as(Elements.OPERAND)
        );
    }

    private static Parser possiblyNegativeTerm() {
        return or(
                sequence(
                        expect(MINUS),
                        term()
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
                expect(LOW_VAR)
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
                Basic.operator(Elements.OPERATOR_SYMBOL_REF).llk(2),
                Basic.parens("minus operator", expect(MINUS)).llk(2),
                Basic.parens("tuple operator", or(expect(Tokens.COMMA_OP), expect(COMMA))).llk(2)
        );
    }

    @NotNull
    private static Sequence fieldSuffix() {
        return sequence("record field suffix",
                expect(Tokens.EQUALS),
                expression
        );
    }

    @NotNull
    private static Sequence variable() {
        return accessible(qualifiedVar(
        ).as(Elements.QUALIFIED_VAR));
    }

    @NotNull
    private static Sequence accessible(Parser parser) {
        return sequence(
                parser,
                many("accessors",
                        expect(DOT),
                        expect(LOW_VAR)
                )
        );
    }

    private static Parser function() {
        return sequence(
                expect(LAMBDA),
                definedValues().as(DEFINED_VALUES),
                spacePrefix(definedValues()).as(DEFINED_VALUES),
                expect(ARROW),
                expression
        ).as(Elements.LAMBDA_EXPRESSION);
    }

    private static Parser case_() {
        return sequence(
                expect(CASE),
                expression,
                expect(OF),
                indentedMany1(caseBranch())
        ).as(CASE_EXPRESSION);
    }

    private static Parser caseBranch() {
        return sequence(
                Pattern.expression.as(DEFINED_VALUES),
                expect(Tokens.ARROW),
                expression
        ).as(Elements.CASE_BRANCH);
    }

    private static Parser if_() {
        return sequence(
                expect(Tokens.IF),
                expression,
                expect(Tokens.THEN),
                expression,
                expect(Tokens.ELSE),
                expression //this should handle else if
        ).as(IF_EXPRESSION);

    }

    private static Parser let() {
        return sequence(
                expect(Tokens.LET),
                Many.indentedMany1(definition()),
                expect(Tokens.IN),
                expression
        ).as(Elements.LET_EXPRESSION);
    }

}
