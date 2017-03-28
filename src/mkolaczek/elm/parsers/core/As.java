package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
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
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        int startingOffset = builder.getCurrentOffset();
        PsiBuilder.Marker marker = builder.mark();
        Result result = content.parse(builder, nextParsers, context);
        if (mode == Mode.MARK_ALWAYS || startingOffset != builder.getCurrentOffset()) {
            marker.done(as);
        } else {
            marker.drop();
        }
        return result;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder, Indentation indentation) {
        return content.willParse(psiBuilder, indentation);
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
