package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;

import java.util.Collection;

public class ConsumeRest implements Parser {

    private final String name;

    @SuppressWarnings("SameParameterValue")
    public static ConsumeRest consumeRest(String name) {
        return new ConsumeRest(name);
    }

    private ConsumeRest(String name) {
        this.name = name;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        if (!builder.eof()) {
            SkipUntil.skipUntil(name, Sets.newHashSet(), builder, context.getIndentation());
        }
        return Result.OK;
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        return WillParseResult.failure();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return name;
    }
}
