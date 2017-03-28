package mkolaczek.elm.parsers.core.context;

public class Context {
    private final Indentation indentation;

    private Context(Indentation indentation) {
        this.indentation = indentation;
    }

    public Indentation getIndentation() {
        return indentation;
    }

    public static Context create() {
        return new Context(Indentation.create());
    }
}
