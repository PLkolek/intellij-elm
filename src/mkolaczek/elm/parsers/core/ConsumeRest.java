package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;

import java.util.Collection;

public class ConsumeRest implements Parser {

    private final String name;

    public static ConsumeRest consumeRest(String name) {
        return new ConsumeRest(name);
    }

    private ConsumeRest(String name) {
        this.name = name;
    }

    @Override
    public Result parse(PsiBuilder psiBuilder, Collection<Parser> nextParsers) {
        if (!psiBuilder.eof()) {
            SkipUntil.skipUntil(name, Sets.newHashSet(), psiBuilder);
        }
        return Result.OK;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder) {
        return false;
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
