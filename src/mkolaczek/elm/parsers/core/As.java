package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class As implements Parser {

    public enum Mode {SKIP_EMPTY, MARK_ALWAYS}

    private final Parser content;
    private final Element as;
    private final Mode mode;

    public As(Parser content, Element as, Mode mode) {
        this.content = content;
        this.as = as;
        this.mode = mode;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        int startingOffset = psiBuilder.getCurrentOffset();
        PsiBuilder.Marker marker = psiBuilder.mark();
        boolean result = content.parse(psiBuilder, nextTokens);
        if (mode == Mode.MARK_ALWAYS || startingOffset != psiBuilder.getCurrentOffset()) {
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
