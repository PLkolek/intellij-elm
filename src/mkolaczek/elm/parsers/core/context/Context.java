package mkolaczek.elm.parsers.core.context;

public class Context {
    private final Indentation indentation = new Indentation();
    private final AsTypes asTypes = new AsTypes();


    public Indentation getIndentation() {
        return indentation;
    }

    public static Context create() {
        return new Context();
    }

    public AsTypes getAsTypes() {
        return asTypes;
    }
}
