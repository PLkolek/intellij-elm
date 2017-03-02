package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Optional;
import java.util.Set;

import static mkolaczek.elm.parsers.core.SkipUntil.skipUntil;

public class Many extends ParserAbstr {

    private final Parser parser;

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
        super(name, true);
        this.parser = parser;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    protected void parse2(PsiBuilder builder, Set<Token> myNextTokens) {

        Set<Token> childNextTokens = Sets.union(myNextTokens, startingTokens());
        do {
            if (startingTokens().contains(builder.getTokenType())) {
                parser.parse(builder, childNextTokens);
            } else if (myNextTokens.contains(builder.getTokenType()) || builder.eof()) {
                return;
            } else {
                Optional<Token> nextValid = SkipUntil.nextValid(childNextTokens, builder);
                if (nextValid.isPresent() && startingTokens().contains(nextValid.get())) {
                    skipUntil(parser.name(), myNextTokens, builder);
                } else {
                    return;
                }
            }
        } while (true);
    }

    @Override
    public Set<Token> startingTokens() {
        return parser.startingTokens();
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
