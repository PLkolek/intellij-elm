package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;

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
    public Result parse(PsiBuilder psiBuilder, Collection<Parser> nextParsers) {
        return containedParser.parse(psiBuilder, nextParsers);
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder) {
        return containedParser.willParse(psiBuilder);
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
