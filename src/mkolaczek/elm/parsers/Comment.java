package mkolaczek.elm.parsers;


import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;

import static mkolaczek.elm.parsers.Combinators.simpleExpect;

//this class should ignore COMMENT_CONTENT, as PsiBuilder can't return it
public class Comment {
    public static NamedParser docComment() {
        Parser parser = builder -> comment(builder,
                Tokens.BEGIN_DOC_COMMENT,
                Tokens.END_DOC_COMMENT,
                Elements.DOC_COMMENT);
        return NamedParser.of("Doc comment", parser);
    }

    private static boolean comment(PsiBuilder builder,
                                   IElementType startToken,
                                   IElementType endToken,
                                   IElementType type) {
        if (builder.getTokenType() != startToken) {
            return false; //short circuit for or in nested comments
        }
        PsiBuilder.Marker m = builder.mark();
        boolean success = simpleExpect(builder, startToken, endToken);
        if (!success) {
            OnError.consumeUntil(builder, Tokens.END_COMMENT);
        }
        m.done(type);
        return success;
    }
}
