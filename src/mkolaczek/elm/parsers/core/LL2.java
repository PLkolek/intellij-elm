package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class LL2 implements Parser {

    private final Parser content;

    public LL2(Parser content) {
        this.content = content;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        if (!contentMatches(psiBuilder)) {
            return false;
        }
        content.parse(psiBuilder, nextTokens);
        return false;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contentMatches(PsiBuilder psiBuilder) {
        IElementType token = psiBuilder.getTokenType();
        IElementType nextToken = psiBuilder.lookAhead(1);
        return !psiBuilder.eof()
                && content.startingTokens().contains(token)
                && content.secondTokens().contains(nextToken);
    }

    @Override
    public Set<Token> startingTokens() {
        return content.startingTokens();
    }

    @Override
    public Set<Token> secondTokens() {
        return content.secondTokens();
    }

    @Override
    public boolean isRequired() {
        return content.isRequired();
    }

    @Override
    public String name() {
        return content.name();
    }
}
