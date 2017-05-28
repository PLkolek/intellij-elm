package mkolaczek.elm.parsers.core;


import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class LL2 implements Parser {

    private final Parser contents;
    private final int lookahead;

    public LL2(Parser contents, int lookahead) {
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
        return contents.willParse(builder, indentation, this.lookahead);
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
