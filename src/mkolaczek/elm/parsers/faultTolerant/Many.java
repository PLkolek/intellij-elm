package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Many extends FTParserAbstr {

    private final FTParser parser;

    public static Many many(String name, FTParser parser) {
        return new Many(name, parser);
    }

    private Many(String name, FTParser parser) {
        super(name, parser.startingTokens(), true, null);
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
