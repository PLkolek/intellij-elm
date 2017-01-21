package mkolaczek.elm.parsers.faultTolerant;

import static mkolaczek.elm.parsers.faultTolerant.FTBasic.dottedCapVar;

public class FTType {

    public static FTParser expression() {
        //TODO
        return app();
    }

    private static FTParser app() {
        //TODO
        return dottedCapVar("type name");
    }
}
