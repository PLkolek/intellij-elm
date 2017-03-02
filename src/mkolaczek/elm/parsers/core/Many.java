package mkolaczek.elm.parsers.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

import static mkolaczek.elm.parsers.core.SkipUntil.skipUntil;

public class Many extends ParserAbstr {

    private final Parser parser;

    public static Sequence many1(Parser parser) {
        return Sequence.sequence(parser.name() + "s",
                parser,
                many(parser)
        );
    }

    public static Many many(Parser parser) {
        return new Many(parser.name() + "s", null, parser);
    }

    public static Many many(String name, Parser... parsers) {
        return new Many(name, null, Sequence.sequence(name, parsers));
    }

    public static Many manyAs(Element as, Parser parser) {
        return new Many(as.getName(), as, parser);
    }


    private Many(String name, Element as, Parser parser) {
        super(name, true, as);
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
                String name = parser.name() + " or " + Joiner.on(", ").join(Token.names(myNextTokens));
                skipUntil(name, childNextTokens, builder);
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
