package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.Expect.expect;
import static mkolaczek.elm.parsers.faultTolerant.Many.many;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequenceAs;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.noWhitespace;

public class FTParsers {
    public static FTParser dottedCapVar(String name, Element moduleName) {
        return sequenceAs(name, moduleName,
                expect(Tokens.CAP_VAR),
                many(name + " parts",
                        sequence(name + " part",
                                noWhitespace(),
                                expect(Tokens.DOT),
                                noWhitespace(),
                                expect(Tokens.CAP_VAR)
                        )
                )
        );
    }


}
