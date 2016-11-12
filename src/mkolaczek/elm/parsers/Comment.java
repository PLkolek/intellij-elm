package mkolaczek.elm.parsers;


import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmElementTypes;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Combinators.expect;
import static mkolaczek.elm.parsers.Combinators.or;
import static mkolaczek.elm.psi.ElmTokenTypes.COMMENT;

//this class should ignore COMMENT_CONTENT, as PsiBuilder can't return it, but, who cares
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
                expect(startToken),
                Combinators.many(or(expect(ElmTokenTypes.COMMENT_CONTENT), Comment.multilineComment())),
                expect(ElmTokenTypes.END_COMMENT)
        );
        if (!success) {
            OnError.consumeUntil(builder, ElmTokenTypes.END_COMMENT);
        }
        m.done(type);
        return success;
    }

    public static NamedParser multilineComment() {
        Parser parser = builder -> comment(builder, ElmTokenTypes.BEGIN_COMMENT, ElmElementTypes.MULTILINE_COMMENT);
        return NamedParser.of("Multiline comment", parser);
    }

    public static NamedParser lineComment() {
        return expect(COMMENT);
    }

    static boolean comments(@NotNull PsiBuilder builder) {
        return Combinators.simpleMany(builder, or(multilineComment(), lineComment()));
    }
}
