package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;

import java.util.Collection;

import static mkolaczek.elm.parsers.core.SkipUntil.anyWillParse;
import static mkolaczek.elm.parsers.core.SkipUntil.willParseAfterSkipping;

public class Try implements Parser {

    private final Parser contents;

    public static Parser tryP(Parser contents) {
        return new Try(contents);
    }

    public Try(Parser contents) {
        this.contents = contents;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> myNextParsers) {
        PsiBuilder.Marker start = builder.mark();
        int startOffset = builder.getCurrentOffset();
        Result result = contents.parse(builder, myNextParsers);
        if (startOffset == builder.getCurrentOffset()) {
            start.rollbackTo();
        } else {
            start.drop();
        }
        if (result == Result.TOKEN_ERROR) {
            if (!anyWillParse(myNextParsers, builder) && willParseAfterSkipping(this, myNextParsers, builder)) {
                SkipUntil.skipUntil(name(), Lists.newArrayList(this), builder);
                return contents.parse(builder, myNextParsers);
            } else {
                return Result.OK;
            }

        }
        return result;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder) {
        return contents.willParse(psiBuilder);
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
