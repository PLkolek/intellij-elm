package mkolaczek.elm.parsers;


import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmTypes;

import static mkolaczek.elm.parsers.Combinators.or;

public class Comment {
    public static NamedParser docComment() {
        Parser parser = builder -> comment(builder, ElmTypes.BEGIN_DOC_COMMENT, ElmTypes.DOC_COMMENT);
        return NamedParser.of("Doc comment", parser);
    }

    private static boolean comment(PsiBuilder builder, IElementType startToken, IElementType type) {
        if (builder.getTokenType() != startToken) {
            return false; //short circuit for or in nested comments
        }
        PsiBuilder.Marker m = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                Combinators.expect(startToken),
                Combinators.many(or(Combinators.expect(ElmTypes.COMMENT_CONTENT), Comment.multilineComment())),
                Combinators.expect(ElmTypes.END_COMMENT)
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTypes.END_COMMENT);
        }
        m.done(type);
        return success;
    }

    private static NamedParser multilineComment() {
        Parser parser = builder -> comment(builder, ElmTypes.BEGIN_COMMENT, ElmTypes.MULTILINE_COMMENT);
        return NamedParser.of("Multiline comment", parser);
    }
}
