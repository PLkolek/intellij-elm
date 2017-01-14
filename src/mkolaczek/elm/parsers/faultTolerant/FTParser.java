package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public interface FTParser {
    boolean parse(PsiBuilder psiBuilder);

    Set<Token> startingTokens();

    boolean isRequired();

    String name();

    Set<Token> nextTokens();

    void computeNextTokens(Set<Token> result);
}
