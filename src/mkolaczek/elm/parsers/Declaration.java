package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.parsers.core.WhiteSpace;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.operatorSymbol;
import static mkolaczek.elm.parsers.Basic.spacePrefix;
import static mkolaczek.elm.parsers.SepBy.pipeSep;
import static mkolaczek.elm.parsers.core.ConsumeRest.consumeRest;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.freshLine;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

public class Declaration {

    public static Parser declarations() {
        return sequence("declarations",
                Many.many1(
                        sequence("declaration",
                                freshLine(declaration())
                        )
                ),
                consumeRest("declaration")
        ).as(Elements.DECLARATIONS);
    }


    private static Parser declaration() {
        return or("declaration",
                Basic.docComment(),
                typeDecl(),
                infixDecl(),
                portDecl(),
                Expression.operatorDefinition().as(Elements.INFIX_OPERATOR_DECLARATION),
                Expression.valueDefinition().as(Elements.VALUE_DECLARATION)
        );
    }

    private static Parser portDecl() {
        return sequence("port declaration",
                expect(Tokens.PORT),
                expect(Tokens.LOW_VAR).as(Elements.PORT_NAME),
                expect(Tokens.COLON),
                Type.expression
        ).separatedBy(WhiteSpace::maybeWhitespace).as(Elements.PORT_DECLARATION);
    }

    private static Parser infixDecl() {
        return sequence("infix operator declaration",
                or(expect(Tokens.INFIXL), expect(Tokens.INFIXR), expect(Tokens.INFIX)),
                expect(Tokens.DIGIT),
                operatorSymbol(Elements.OPERATOR_SYMBOL)
        ).separatedBy(WhiteSpace::maybeWhitespace).as(Elements.INFIX_OPERATOR_DECLARATION);

    }

    private static Parser typeDecl() {
        return sequence(
                expect(Tokens.TYPE),
                maybeWhitespace(or("type declaration contents",
                        typeAliasDecl(),
                        typeDeclContents()
                ))
        ).as(Elements.TYPE_DECLARATION);
    }

    private static Parser typeDeclContents() {
        return sequence("type declaration contents suffix",
                nameArgs(),
                sequence(
                        maybeWhitespace(expect(Tokens.EQUALS)),
                        maybeWhitespace(pipeSep(Type.unionConstructor()))
                ).as(Elements.TYPE_DECL_DEF_NODE)
        ).as(Elements.TYPE_DECL_NODE);
    }

    private static Parser typeAliasDecl() {
        return sequence("type alias declaration",
                expect(Tokens.ALIAS),
                maybeWhitespace(nameArgs()),
                maybeWhitespace(expect(Tokens.EQUALS)),
                maybeWhitespace(Type.expression)
        ).as(Elements.TYPE_ALIAS_DECL_NODE);
    }

    @NotNull
    private static Sequence nameArgs() {
        return sequence("type and equals",
                expect(Tokens.CAP_VAR).as(Elements.TYPE_NAME),
                spacePrefix(expect(Tokens.LOW_VAR))
        );
    }

}
