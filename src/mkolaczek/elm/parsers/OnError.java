package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

public class OnError {
    public static void consumeUntil(PsiBuilder builder, IElementType token) {
        while (!builder.eof() && builder.getTokenType() != token) {
            builder.advanceLexer();
        }
        if (!builder.eof()) {
            builder.advanceLexer();
        }
    }
}
