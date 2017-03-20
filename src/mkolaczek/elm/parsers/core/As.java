package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;

import java.util.Collection;

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
    public Result parse(PsiBuilder psiBuilder, Collection<Parser> nextParsers) {
        int startingOffset = psiBuilder.getCurrentOffset();
        PsiBuilder.Marker marker = psiBuilder.mark();
        Result result = content.parse(psiBuilder, nextParsers);
        if (mode == Mode.MARK_ALWAYS || startingOffset != psiBuilder.getCurrentOffset()) {
            marker.done(as);
        } else {
            marker.drop();
        }
        return result;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder) {
        return content.willParse(psiBuilder);
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
