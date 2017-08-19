package mkolaczek.elm.parsers.core;


import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class LLK implements Parser {

    private final Parser contents;
    private final int lookahead;

    public LLK(Parser contents, int lookahead) {
        this.contents = contents;
        this.lookahead = lookahead;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        if (willParse(builder, context.getIndentation())) {
            return contents.parse(builder, nextParsers, context);
        }
        return Result.TOKEN_ERROR;
    }

    @NotNull
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public WillParseResult willParse(PsiBuilder builder, Indentation indentation, int lookahead) {
        Preconditions.checkArgument(lookahead == 1);
        PsiBuilder.Marker marker = builder.mark();
        WillParseResult result = contents.willParse(builder, indentation, this.lookahead);
        marker.rollbackTo();
        return result;
    }

    @Override
    public boolean isRequired() {
        return contents.isRequired();
    }

    @Override
    public String name() {
        return contents.name();
    }
}
