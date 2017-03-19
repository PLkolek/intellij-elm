package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class ConsumeRest implements Parser {

    private final String name;

    public static ConsumeRest consumeRest(String name) {
        return new ConsumeRest(name);
    }

    private ConsumeRest(String name) {
        this.name = name;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        if (!psiBuilder.eof()) {
            SkipUntil.skipUntil(name, Sets.newHashSet(), psiBuilder);
        }
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return Sets.newHashSet();
    }

    @Override
    public Set<Token> secondTokens() {
        throw new UnsupportedOperationException("Consume rest has no second tokens");
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
