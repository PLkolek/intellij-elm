package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public abstract class ParserAbstr implements Parser {

    protected final String name;
    protected final Element as;
    private final boolean isRoot;
    private Set<Token> nextTokens;

    protected ParserAbstr(String name, boolean root, Element as) {
        this.name = name;
        this.as = as;
        this.isRoot = root;
    }

    protected ParserAbstr(String name, Element as) {
        this(name, false, as);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder) {
        //noinspection SuspiciousMethodCalls
        if (!isRoot && (psiBuilder.eof() || !startingTokens().contains(psiBuilder.getTokenType()))) {
            return !isRequired();
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
        if (nextTokens == null) {
            this.nextTokens = Sets.union(myNextTokens, startingTokens());
            computeNextTokens2(myNextTokens);
        }
    }

    @Override
    public Set<Token> nextTokens() {
        return nextTokens;
    }


    protected abstract void parse2(PsiBuilder builder);

    protected abstract void computeNextTokens2(Set<Token> myNextTokens);
}
