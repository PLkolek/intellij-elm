package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.FTBasic.padded;
import static mkolaczek.elm.parsers.faultTolerant.Many.many;
import static mkolaczek.elm.parsers.faultTolerant.Many.many1;
import static mkolaczek.elm.parsers.faultTolerant.Or.or;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.Try.tryP;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.forcedWhitespace;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.freshLine;

public class FTDeclaration {

    public static FTParser declarations() {
        return many1(sequence("declaration",
                freshLine(),
                declaration()
                )
        );
    }


    private static FTParser declaration() {
        return or("declaration", FTBasic.docComment(), typeDecl());
    }

    private static FTParser typeDecl() {
        return sequenceAs(Elements.TYPE_DECLARATION,
                expect(Tokens.TYPE),
                forcedWhitespace(),
                typeAliasDecl()
        );
    }

    private static FTParser typeAliasDecl() {
        return sequence("type alias declaration",
                expect(Tokens.ALIAS),
                forcedWhitespace(),
                expect(Tokens.CAP_VAR),
                spacePrefix(expect(Tokens.LOW_VAR)),
                padded(Tokens.EQUALS),
                FTType.expression()
        );
    }

    private static FTParser spacePrefix(FTParser parser) {
        return many(String.format("space prefixed list of %ss", parser.name()),
                tryP(
                        sequence("space prefixed " + parser.name(),
                                forcedWhitespace(),
                                parser)
                )
        );
    }
}
