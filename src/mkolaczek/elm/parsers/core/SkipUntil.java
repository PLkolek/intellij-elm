package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Token;

import java.util.Optional;
import java.util.Set;

public class SkipUntil {

    public static void skipUntil(String expectedName, Set<Token> nextTokens, PsiBuilder builder) {
        PsiBuilder.Marker errorStart = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        while (!builder.eof()) {
            //noinspection SuspiciousMethodCalls
            if (nextTokens.contains(builder.getTokenType())) {
                errorStart.error(expectedName + " expected");
                return;
            }
            builder.advanceLexer();
        }
        if (startingOffset != builder.getCurrentOffset()) {
            errorStart.error(expectedName + " expected");
        } else {
            errorStart.drop();
            builder.error(expectedName + " expected");
        }
    }

    public static Optional<Token> nextValid(Set<Token> nextTokens, PsiBuilder builder) {
        PsiBuilder.Marker errorStart = builder.mark();
        while (!builder.eof()) {
            IElementType token = builder.getTokenType();
            //noinspection SuspiciousMethodCalls
            if (nextTokens.contains(token)) {
                errorStart.rollbackTo();
                return Optional.of((Token) token);
            }
            builder.advanceLexer();
        }
        errorStart.rollbackTo();
        return Optional.empty();
    }
}
