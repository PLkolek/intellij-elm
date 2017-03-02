package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Optional;
import java.util.Set;

import static mkolaczek.elm.parsers.core.SkipUntil.skipUntil;

public class Many implements Parser {

    private final Parser parser;
    private final String name;

    public static Parser many1(Parser parser) {
        return Sequence.sequence(
                parser,
                many(parser)
        );
    }

    public static Many many(Parser parser) {
        return new Many(parser.name() + "s", parser);
    }

    public static Many many(String name, Parser... parsers) {
        return new Many(name, Sequence.sequence(name, parsers));
    }

    private Many(String name, Parser parser) {
        this.name = name;
        this.parser = parser;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean parse(PsiBuilder builder, Set<Token> myNextTokens) {
        Set<Token> childNextTokens = Sets.union(myNextTokens, startingTokens());
        do {
            if (startingTokens().contains(builder.getTokenType())) {
                parser.parse(builder, childNextTokens);
            } else if (myNextTokens.contains(builder.getTokenType()) || builder.eof()) {
                break;
            } else {
                Optional<Token> nextValid = SkipUntil.nextValid(childNextTokens, builder);
                if (nextValid.isPresent() && startingTokens().contains(nextValid.get())) {
                    skipUntil(parser.name(), myNextTokens, builder);
                } else {
                    break;
                }
            }
        } while (true);
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return parser.startingTokens();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return name;
    }
}
