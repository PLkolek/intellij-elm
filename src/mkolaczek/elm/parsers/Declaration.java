package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Sequence;
import mkolaczek.elm.parsers.core.WhiteSpace;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.*;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.forcedWhitespace;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

public class Declaration {

    public static Parser declarations() {
        return Many.many1As(Elements.DECLARATIONS,
                sequence("declaration",
                        WhiteSpace.freshLine(),
                        declaration()
                )
        );
    }


    private static Parser declaration() {
        return or("declaration", Basic.docComment(), typeDecl());
    }

    private static Parser typeDecl() {
        return Sequence.sequenceAs(Elements.TYPE_DECLARATION,
                expect(Tokens.TYPE),
                forcedWhitespace(),
                or("type declaration contents",
                        typeAliasDecl(),
                        typeDeclContents()
                )
        );
    }

    private static Parser typeDeclContents() {
        return sequence("type declaration contents suffix",
                nameArgsEquals(),
                sepBy(Tokens.PIPE, Type.unionConstructor())
        );
    }

    private static Parser typeAliasDecl() {
        return sequence("type alias declaration",
                expect(Tokens.ALIAS),
                forcedWhitespace(),
                nameArgsEquals(),
                Type.expression
        );
    }

    @NotNull
    private static Sequence nameArgsEquals() {
        return sequence("type and equals",
                expect(Tokens.CAP_VAR),
                spacePrefix(expect(Tokens.LOW_VAR)),
                padded(Tokens.EQUALS),
                maybeWhitespace()
        );
    }

}
