package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;

import java.util.Collection;

public class ParserBox implements Parser {

    private final String name;
    private Parser containedParser = null;

    public ParserBox(String name) {
        this.name = name;
    }

    public void setParser(Parser parser) {
        Preconditions.checkState(containedParser == null);
        this.containedParser = parser;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        return containedParser.parse(builder, nextParsers, context);
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        return containedParser.willParse(psiBuilder, indentation, lookahead);
    }

    @Override
    public boolean isRequired() {
        return containedParser.isRequired();
    }

    @Override
    public String name() {
        return name;
    }

}
