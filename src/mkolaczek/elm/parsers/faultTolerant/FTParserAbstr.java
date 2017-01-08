package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public abstract class FTParserAbstr implements FTParser {

    protected final String name;
    protected final Set<Token> startingTokens;
    protected final boolean isOptional;
    private final Element as;
    private Set<Token> nextTokens;

    protected FTParserAbstr(String name, Set<Token> startingTokens, boolean isOptional, Element as) {
        this.name = name;
        this.startingTokens = startingTokens;
        this.isOptional = isOptional;
        this.as = as;
    }

    @Override
    public Set<Token> startingTokens() {
        return startingTokens;
    }

    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder) {
        //noinspection SuspiciousMethodCalls
        if (!startingTokens.contains(psiBuilder.getTokenType())) {
            return false;
        }
        PsiBuilder.Marker marker = as != null ? psiBuilder.mark() : null;
        parse2(psiBuilder);
        if (marker != null) {
            marker.done(as);
        }
        return true;
    }

    @Override
    public void computeNextTokens(Set<Token> myNextTokens) {
        computeNextTokens2(myNextTokens);
        this.nextTokens = Sets.union(myNextTokens, startingTokens);
    }

    @Override
    public Set<Token> nextTokens() {
        return nextTokens;
    }


    protected abstract void parse2(PsiBuilder builder);

    protected abstract void computeNextTokens2(Set<Token> myNextTokens);
}
