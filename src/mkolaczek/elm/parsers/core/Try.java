package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static mkolaczek.elm.parsers.core.SkipUntil.anyWillParse;

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
        Result result = contents.parse(builder, myNextParsers, context);
        if(result == Result.OK) {
            return Result.OK;
        } else if(!anyWillParse(myNextParsers, builder, context.getIndentation())) {
            List<Parser> next = Lists.newArrayList(myNextParsers);
            next.add(this);
            SkipUntil.skipUntil(myNextParsers.iterator().next().name(), next, builder, context.getIndentation());
            return contents.parse(builder, myNextParsers, context);
        }
        return Result.SKIPPED;
    }

    @NotNull
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
