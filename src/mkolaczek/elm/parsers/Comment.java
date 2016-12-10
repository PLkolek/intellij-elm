package mkolaczek.elm.parsers;


import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Combinators.expect;
import static mkolaczek.elm.parsers.Combinators.or;
import static mkolaczek.elm.psi.Tokens.COMMENT;

//this class should ignore COMMENT_CONTENT, as PsiBuilder can't return it, but, who cares
public class Comment {
    public static NamedParser docComment() {
        Parser parser = builder -> comment(builder, Tokens.BEGIN_DOC_COMMENT, Elements.DOC_COMMENT);
        return NamedParser.of("Doc comment", parser);
    }

    private static boolean comment(PsiBuilder builder, IElementType startToken, IElementType type) {
        if (builder.getTokenType() != startToken) {
            return false; //short circuit for or in nested comments
        }
        PsiBuilder.Marker m = builder.mark();
        boolean success = Combinators.simpleSequence(builder,
                expect(startToken),
                Combinators.many(or(expect(Tokens.COMMENT_CONTENT), Comment.multilineComment())),
                expect(Tokens.END_COMMENT)
        );
        if (!success) {
            OnError.consumeUntil(builder, Tokens.END_COMMENT);
        }
        m.done(type);
        return success;
    }

    public static NamedParser multilineComment() {
        Parser parser = builder -> comment(builder, Tokens.BEGIN_COMMENT, Elements.MULTILINE_COMMENT);
        return NamedParser.of("Multiline comment", parser);
    }

    public static NamedParser lineComment() {
        return expect(COMMENT);
    }

    static boolean comments(@NotNull PsiBuilder builder) {
        return Combinators.simpleMany(builder, or(multilineComment(), lineComment()));
    }
}
