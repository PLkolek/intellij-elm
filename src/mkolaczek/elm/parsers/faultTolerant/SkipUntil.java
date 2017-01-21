package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

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
}
