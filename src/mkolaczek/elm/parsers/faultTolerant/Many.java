package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;

public class Many extends FTParserAbstr {

    private final FTParser parser;

    public static Many many(String name, FTParser parser) {
        return new Many(name, null, parser);
    }

    public static Many many(String name, FTParser... parsers) {
        return new Many(name, null, sequence(name, parsers));
    }

    public static Many manyAs(Element as, FTParser parser) {
        return new Many(as.getName(), as, parser);
    }


    private Many(String name, Element as, FTParser parser) {
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
