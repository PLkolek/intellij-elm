package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public interface Parser {
    boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens);

    Set<Token> startingTokens();

    boolean isRequired();

    String name();

}
