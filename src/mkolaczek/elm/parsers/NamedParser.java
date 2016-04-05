package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;

public class NamedParser implements Parser {
    private final String name;
    private final Parser p;

    private NamedParser(String name, Parser p) {

        this.name = name;
        this.p = p;
    }

    String name() {
        return name;
    }

    public static NamedParser of(String name, Parser p) {
        return new NamedParser(name, p);
    }

    @Override
    public Boolean apply(PsiBuilder builder) {
        return p.apply(builder);
    }

    public NamedParser withParser(Parser parser) {
        return new NamedParser(name, parser);
    }
}
