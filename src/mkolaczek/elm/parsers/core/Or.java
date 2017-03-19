package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

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

    private static Set<Token> startingTokens(Parser... parsers) {
        Set<Token> result = Sets.newHashSet();
        for (Parser parser : parsers) {
            result.addAll(parser.startingTokens());
        }
        return result;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> myNextTokens) {
        //noinspection SuspiciousMethodCalls
        if (psiBuilder.eof() || !startingTokens().contains(psiBuilder.getTokenType())) {
            return false;
        }
        for (Parser parser : parsers) {
            if (parser.parse(psiBuilder, myNextTokens)) {
                break;
            }
        }
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return startingTokens(parsers);
    }

    @Override
    public Set<Token> secondTokens() {
        throw new UnsupportedOperationException("Hopefully this won't be necessary");
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
