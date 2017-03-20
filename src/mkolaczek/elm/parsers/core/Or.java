package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class Or implements Parser {

    private final String name;
    private final Parser[] parsers;

    public static Or or(String name, Parser... parsers) {
        return new Or(name, parsers);
    }

    public static Or or(Parser... parsers) {
        return new Or(name(parsers), parsers);
    }


    private static String name(Parser[] parsers) {
        Preconditions.checkArgument(parsers.length > 0);
        String joinedNames = stream(parsers).map(Parser::name).collect(joining(", "));
        return String.format("One of [%s]", joinedNames);
    }

    private Or(String name, Parser... parsers) {
        Preconditions.checkArgument(Parser.allRequired(parsers), "No parser in OR should be optional");
        this.name = name;
        this.parsers = parsers;
    }

    @Override
    public boolean willParse(PsiBuilder builder) {
        for (Parser parser : parsers) {
            if (parser.willParse(builder)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Collection<Parser> myNextParsers) {
        //noinspection SuspiciousMethodCalls
        if (psiBuilder.eof()) {
            return false;
        }
        for (Parser parser : parsers) {
            if (parser.parse(psiBuilder, myNextParsers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public String name() {
        return name;
    }
}
