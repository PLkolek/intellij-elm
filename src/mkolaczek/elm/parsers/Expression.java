package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.ParserBox;
import mkolaczek.elm.parsers.core.Sequence;
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
        return or("definition",
                valueDefinition,
                sequence(
                        Pattern.term(),
                        definitionEnd()
                )
        );
    }

    private static Parser definitionEnd() {
        return sequence("definitionEnd",
                spacePrefix(Pattern.term()),
                maybeWhitespace(expect(EQUALS)),
                expression
        );
    }

    private static final ParserBox expression = new ParserBox("expression");

    static {
        //TODO: implement
        expression.setParser(
                or("expression",
                        let(),
                        if_(),
                        case_(),
                        function(),
                        termOperation()
                )
        );
    }

    private static Parser termOperation() {
        //TODO: continue expression end
        return sequence(
                possiblyNegativeTerm(),
                spacePrefix(term())
        );
    }

    private static Parser possiblyNegativeTerm() {
        return sequence(
                tryP(expect(MINUS)),
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
        );
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
                Basic.operator().ll2(newHashSet(LPAREN), newHashSet(SYM_OP)),
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
        return accessible(dottedVar("qualified variable"));
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
