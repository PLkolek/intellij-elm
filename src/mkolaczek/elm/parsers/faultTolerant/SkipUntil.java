package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class SkipUntil {

    public static void skipUntil(String expectedName, Set<Token> nextTokens, PsiBuilder builder) {
        PsiBuilder.Marker errorStart = builder.mark();
        while (!builder.eof()) {
            //noinspection SuspiciousMethodCalls
            if (nextTokens.contains(builder.getTokenType())) {
                errorStart.error(expectedName + " expected");
                return;
            }
            builder.advanceLexer();
        }
        errorStart.error(expectedName + " expected");
    }
}
