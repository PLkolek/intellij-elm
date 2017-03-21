package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Indentation;

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
    public boolean willParse(PsiBuilder builder, Indentation indentation) {
        for (Parser parser : parsers) {
            if (parser.willParse(builder, indentation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> myNextParsers, Indentation indentation) {
        //noinspection SuspiciousMethodCalls
        if (builder.eof()) {
            return Result.TOKEN_ERROR;
        }
        for (Parser parser : parsers) {
            if (parser.parse(builder, myNextParsers, indentation) == Result.OK) {
                return Result.OK;
            }
        }
        return Result.TOKEN_ERROR;
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
