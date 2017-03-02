package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public abstract class ParserAbstr implements Parser {

    protected final String name;
    private final boolean isRoot;

    protected ParserAbstr(String name, boolean root) {
        this.name = name;
        this.isRoot = root;
    }

    protected ParserAbstr(String name) {
        this(name, false);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        //noinspection SuspiciousMethodCalls
        if (!isRoot && (psiBuilder.eof() || !startingTokens().contains(psiBuilder.getTokenType()))) {
            return !isRequired();
        }
        parse2(psiBuilder, nextTokens);
        return true;
    }


    protected abstract void parse2(PsiBuilder builder, Set<Token> nextTokens);
}
