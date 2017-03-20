package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;

import java.util.Collection;

public class SkipUntil {

    public static void skipUntil(String expectedName, Collection<Parser> nextParsers, PsiBuilder builder) {
        PsiBuilder.Marker errorStart = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        while (!builder.eof()) {
            //noinspection SuspiciousMethodCalls
            if (anyWillParse(nextParsers, builder)) {
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

    public static boolean willParseAfterSkipping(Parser me, Collection<Parser> nextParsers, PsiBuilder builder) {
        PsiBuilder.Marker errorStart = builder.mark();
        while (!builder.eof()) {
            //noinspection SuspiciousMethodCalls
            if (anyWillParse(nextParsers, builder)) {
                boolean result = me.willParse(builder);
                errorStart.rollbackTo();
                return result;
            }
            builder.advanceLexer();
        }
        errorStart.rollbackTo();
        return false;
    }

    public static boolean anyWillParse(Collection<Parser> nextParsers, PsiBuilder builder) {
        return nextParsers.stream().anyMatch(p -> p.willParse(builder));
    }
}
