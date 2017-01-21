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
        super(contents.name(), contents.startingTokens(), true, null);
        this.contents = contents;
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        contents.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        contents.computeNextTokens(myNextTokens);
    }
}
