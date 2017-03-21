package mkolaczek.elm.features.syntaxHinglighting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.lexer.ElmLexerAdapter;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ElmSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Set<IElementType> KEYWORDS = Sets.newHashSet(
            Tokens.MODULE,
            Tokens.AS,
            Tokens.EXPOSING,
            Tokens.IMPORT,
            Tokens.PORT,
            Tokens.EFFECT,
            Tokens.WHERE,
            Tokens.TYPE,
            Tokens.ALIAS,
            Tokens.INFIXL,
            Tokens.INFIXR,
            Tokens.INFIX,
            Tokens.LET,
            Tokens.IN,
            Tokens.IF,
            Tokens.THEN,
            Tokens.ELSE,
            Tokens.CASE,
            Tokens.OF
    );

    private static final Set<IElementType> VALID_ESCAPES = ImmutableSet.of(
            Tokens.VALID_STRING_ESCAPE_TOKEN
    );

    private static final Set<IElementType> INVALID_ESCAPES = ImmutableSet.of(
            Tokens.INVALID_CHARACTER_ESCAPE_TOKEN,
            Tokens.INVALID_UNICODE_ESCAPE_TOKEN
    );

    private static final Set<IElementType> STRING_LITERALS = ImmutableSet.of(
            Tokens.MULTILINE_STRING,
            Tokens.QUOTE,
            Tokens.STRING_CONTENT
    );


    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ElmLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (KEYWORDS.contains(tokenType)) {
            return new TextAttributesKey[]{DefaultLanguageHighlighterColors.KEYWORD};
        }
        if (Tokens.COMMENT_TOKENS.contains(tokenType)) {
            return new TextAttributesKey[]{DefaultLanguageHighlighterColors.LINE_COMMENT};
        }
        if (STRING_LITERALS.contains(tokenType)) {
            return new TextAttributesKey[]{DefaultLanguageHighlighterColors.STRING};
        }
        if (VALID_ESCAPES.contains(tokenType)) {
            return new TextAttributesKey[]{DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE};
        }
        if (INVALID_ESCAPES.contains(tokenType)) {
            return new TextAttributesKey[]{DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE};
        }
        return EMPTY_KEYS;
    }

}
