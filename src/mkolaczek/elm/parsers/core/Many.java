package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

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
        super(name, parser.startingTokens(), true, as);
        this.parser = parser;
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        //noinspection SuspiciousMethodCalls
        while (parser.startingTokens().contains(builder.getTokenType())) {
            parser.parse(builder);
        }
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        this.parser.computeNextTokens(myNextTokens);
    }
}
