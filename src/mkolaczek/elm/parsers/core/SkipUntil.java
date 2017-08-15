package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Indentation;

import java.util.Collection;

class SkipUntil {

    public static void skipUntil(String expectedName,
                                 Collection<Parser> nextParsers,
                                 PsiBuilder builder,
                                 Indentation indentation) {
        PsiBuilder.Marker errorStart = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        while (!builder.eof()) {
            //noinspection SuspiciousMethodCalls
            if (anyWillParse(nextParsers, builder, indentation)) {
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

    public static boolean anyWillParse(Collection<Parser> nextParsers, PsiBuilder builder, Indentation indentation) {
        return nextParsers.stream().anyMatch(p -> p.willParse(builder, indentation));
    }
}
