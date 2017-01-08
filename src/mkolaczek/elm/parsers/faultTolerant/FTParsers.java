package mkolaczek.elm.parsers.faultTolerant;

import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.NO;

public class FTParsers {
    public static FTParser dottedCapVar(String name, Element moduleName) {
        return new Sequence(name, moduleName,
                new Expect(Tokens.CAP_VAR),
                new Many(name + " parts",
                        new Sequence(name + " part",
                                new WhiteSpace(NO),
                                new Expect(Tokens.DOT),
                                new WhiteSpace(NO),
                                new Expect(Tokens.CAP_VAR)
                        )
                )
        );
    }
}
