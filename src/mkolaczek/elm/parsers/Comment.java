package mkolaczek.elm.parsers;


import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmTokenTypes;
import mkolaczek.elm.psi.ElmElementTypes;

import static mkolaczek.elm.parsers.Combinators.or;

public class Comment {
    public static NamedParser docComment() {
        Parser parser = builder -> comment(builder, ElmTokenTypes.BEGIN_DOC_COMMENT, ElmElementTypes.DOC_COMMENT);
        return NamedParser.of("Doc comment", parser);
    }

    private static boolean comment(PsiBuilder builder, IElementType startToken, IElementType type) {
        if (builder.getTokenType() != startToken) {
            return false; //short circuit for or in nested comments
        }
        PsiBuilder.Marker m = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.expect(startToken),
                Combinators.many(or(Combinators.expect(ElmTokenTypes.COMMENT_CONTENT), Comment.multilineComment())),
                Combinators.expect(ElmTokenTypes.END_COMMENT)
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTokenTypes.END_COMMENT);
        }
        m.done(type);
        return success;
    }

    private static NamedParser multilineComment() {
        Parser parser = builder -> comment(builder, ElmTokenTypes.BEGIN_COMMENT, ElmElementTypes.MULTILINE_COMMENT);
        return NamedParser.of("Multiline comment", parser);
    }
}
