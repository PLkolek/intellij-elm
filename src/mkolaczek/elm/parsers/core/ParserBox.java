package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

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
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        return containedParser.parse(psiBuilder, nextTokens);
    }

    @Override
    public Set<Token> startingTokens() {
        return containedParser.startingTokens();
    }

    @Override
    public Set<Token> secondTokens() {
        return containedParser.secondTokens();
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
