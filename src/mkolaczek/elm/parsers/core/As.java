package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
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
        context.getAsTypes().push(as);
        Result result = content.parse(builder, nextParsers, context);
        Element stackAs = context.getAsTypes().pop();
        if (mode == Mode.MARK_ALWAYS || startingOffset != builder.getCurrentOffset()) {
            marker.done(stackAs);
        } else {
            marker.drop();
        }
        return result;
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        return content.willParse(psiBuilder, indentation, lookahead);
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
