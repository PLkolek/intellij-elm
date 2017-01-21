package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.*;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

public class Declaration {

    public static Parser declarations() {
        return Many.many1(Sequence.sequence("declaration",
                WhiteSpace.freshLine(),
                declaration()
                )
        );
    }


    private static Parser declaration() {
        return Or.or("declaration", Basic.docComment(), typeDecl());
    }

    private static Parser typeDecl() {
        return Sequence.sequenceAs(Elements.TYPE_DECLARATION,
                Expect.expect(Tokens.TYPE),
                WhiteSpace.forcedWhitespace(),
                typeAliasDecl()
        );
    }

    private static Parser typeAliasDecl() {
        return Sequence.sequence("type alias declaration",
                Expect.expect(Tokens.ALIAS),
                WhiteSpace.forcedWhitespace(),
                Expect.expect(Tokens.CAP_VAR),
                spacePrefix(Expect.expect(Tokens.LOW_VAR)),
                Basic.padded(Tokens.EQUALS),
                Type.expression()
        );
    }

    private static Parser spacePrefix(Parser parser) {
        return Many.many(String.format("space prefixed list of %ss", parser.name()),
                Try.tryP(
                        Sequence.sequence("space prefixed " + parser.name(),
                                WhiteSpace.forcedWhitespace(),
                                parser)
                )
        );
    }
}
