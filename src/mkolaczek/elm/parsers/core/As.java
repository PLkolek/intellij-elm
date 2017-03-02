package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class As implements Parser {


    private final Parser content;
    private final Element as;

    public As(Parser content, Element as) {
        this.content = content;
        this.as = as;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        int startingOffset = psiBuilder.getCurrentOffset();
        PsiBuilder.Marker marker = psiBuilder.mark();
        boolean result = content.parse(psiBuilder, nextTokens);
        if (startingOffset != psiBuilder.getCurrentOffset()) {
            marker.done(as);
        } else {
            marker.drop();
        }
        return result;
    }

    @Override
    public Set<Token> startingTokens() {
        return content.startingTokens();
    }

    @Override
    public boolean isRequired() {
        return content.isRequired();
    }

    @Override
    public String name() {
        return as.getName();
    }
}
