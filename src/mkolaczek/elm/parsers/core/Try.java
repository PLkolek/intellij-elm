package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Try extends ParserAbstr {

    private final Parser contents;

    public static Parser tryP(Parser contents) {
        return new Try(contents);
    }

    public Try(Parser contents) {
        super(contents.name(), true, null);
        this.contents = contents;
    }

    @Override
    protected void parse2(PsiBuilder builder, Set<Token> myNextTokens) {
        //noinspection SuspiciousMethodCalls
        if (contents.startingTokens().contains(builder.getTokenType())) {
            contents.parse(builder, myNextTokens);
        }
    }

    @Override
    public Set<Token> startingTokens() {
        return contents.startingTokens();
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
