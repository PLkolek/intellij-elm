package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;

import java.util.Collection;

import static mkolaczek.elm.parsers.core.SkipUntil.anyWillParse;
import static mkolaczek.elm.parsers.core.SkipUntil.willParseAfterSkipping;

public class Try implements Parser {

    private final Parser contents;

    public static Parser tryP(Parser contents) {
        return new Try(contents);
    }

    private Try(Parser contents) {
        this.contents = contents;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> myNextParsers, Context context) {
        PsiBuilder.Marker start = builder.mark();
        int startOffset = builder.getCurrentOffset();
        Result result = contents.parse(builder, myNextParsers, context);
        if (startOffset == builder.getCurrentOffset()) {
            start.rollbackTo();
        } else {
            start.drop();
        }
        if (result == Result.TOKEN_ERROR) {
            if (!anyWillParse(myNextParsers, builder, context.getIndentation()) && willParseAfterSkipping(this,
                    myNextParsers,
                    builder,
                    context.getIndentation())) {
                SkipUntil.skipUntil(name(), Lists.newArrayList(this), builder, context.getIndentation());
                return contents.parse(builder, myNextParsers, context);
            } else {
                return Result.OK;
            }

        }
        return result;
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        return contents.willParse(psiBuilder, indentation, lookahead);
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return contents.name();
    }
}
