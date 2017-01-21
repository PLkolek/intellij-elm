package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Parser;

public class Type {

    public static Parser expression() {
        //TODO
        return app();
    }

    private static Parser app() {
        //TODO
        return Basic.dottedCapVar("type name");
    }
}
