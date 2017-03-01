package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public abstract class ParserAbstr implements Parser {

    protected final String name;
    protected final Element as;
    private final boolean isRoot;

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
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        //noinspection SuspiciousMethodCalls
        if (!isRoot && (psiBuilder.eof() || !startingTokens().contains(psiBuilder.getTokenType()))) {
            return !isRequired();
        }
        PsiBuilder.Marker marker = as != null ? psiBuilder.mark() : null;
        parse2(psiBuilder, nextTokens);
        if (marker != null) {
            marker.done(as);
        }
        return true;
    }


    protected abstract void parse2(PsiBuilder builder, Set<Token> nextTokens);
}
