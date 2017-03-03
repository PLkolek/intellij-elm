package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public interface Parser {
    boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens);

    Set<Token> startingTokens();

    boolean isRequired();

    String name();

    default Parser as(Element as) {
        return new As(this, as);
    }

    //must be called before as, it doesn't make sense the other way around
    //TODO: encode this in types
    default Parser absorbingErrors() {
        return new AbsorbingErrors(this);
    }

}
