package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.psi.Elements;

import static mkolaczek.elm.parsers.Basic.brackets;
import static mkolaczek.elm.parsers.Basic.parens;
import static mkolaczek.elm.parsers.SepBy.commaSep;
import static mkolaczek.elm.parsers.SepBy.tryCommaSep;
import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Or.or;
import static mkolaczek.elm.parsers.core.Try.tryP;
import static mkolaczek.elm.psi.Tokens.LOW_VAR;

public class Pattern {
    public static Parser term() {
        //TODO: continue
        return or(
                record(),
                tuple()
        );
    }

    private static Parser record() {
        return brackets(tryCommaSep(expect(LOW_VAR)));
    }

    private static Parser tuple() {
        //TODO: implement
        return parens("tuple type",
                tryP(commaSep(expect(LOW_VAR)).as(Elements.SURROUND_CONTENTS))
        );
    }


}
