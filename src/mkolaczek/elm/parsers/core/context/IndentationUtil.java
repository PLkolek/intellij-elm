package mkolaczek.elm.parsers.core.context;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Tokens;

import java.util.Optional;

public class IndentationUtil {

    public static int column(PsiBuilder builder) {
        int i = findBackwards(0, builder);
        return builder.getCurrentOffset() - i;
    }

    public static Optional<Integer> indent(PsiBuilder builder) {
        int newLineIndex = findBackwards(whitespaceStart(builder), builder);
        if (newLineIndex >= 0 && builder.getOriginalText().charAt(newLineIndex) != '\n') {
            return Optional.empty();
        }
        return Optional.of(builder.getCurrentOffset() - newLineIndex);
    }

    private static int whitespaceStart(PsiBuilder builder) {
        int nonWsOrCommentTokenIndex = 0;
        IElementType token;
        do {
            nonWsOrCommentTokenIndex--;
            token = builder.rawLookup(nonWsOrCommentTokenIndex);
        } while (token == TokenType.WHITE_SPACE || Tokens.COMMENT_TOKENS.contains(token));
        return builder.rawTokenTypeStart(nonWsOrCommentTokenIndex + 1);
    }

    private static int findBackwards(int wsStart, PsiBuilder builder) {
        CharSequence text = builder.getOriginalText();
        int i = builder.getCurrentOffset() - 1;
        while (i >= wsStart && text.charAt(i) != '\n') {
            i--;
        }
        return i;
    }
}
