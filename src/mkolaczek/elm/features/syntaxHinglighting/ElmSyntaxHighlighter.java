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

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

class ElmSyntaxHighlighter extends SyntaxHighlighterBase {

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

    private static final Set<IElementType> GLSL_TOKENS = ImmutableSet.of(
            Tokens.BEGIN_GLSL,
            Tokens.GLSL_CONTENT,
            Tokens.END_GLSL
    );


    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    public static final TextAttributesKey ELM_ARROW =
            createTextAttributesKey("ELM_ARROW", DefaultLanguageHighlighterColors.OPERATION_SIGN);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ElmLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (KEYWORDS.contains(tokenType)) {
            return pack(DefaultLanguageHighlighterColors.KEYWORD);
        }
        if (Tokens.COMMENT_TOKENS.contains(tokenType)) {
            return pack(DefaultLanguageHighlighterColors.LINE_COMMENT);
        }
        if (STRING_LITERALS.contains(tokenType)) {
            return pack(DefaultLanguageHighlighterColors.STRING);
        }
        if (VALID_ESCAPES.contains(tokenType)) {
            return pack(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
        }
        if (INVALID_ESCAPES.contains(tokenType)) {
            return pack(DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
        }
        if (GLSL_TOKENS.contains(tokenType)) {
            return pack(DefaultLanguageHighlighterColors.STRING);
        }
        if (tokenType == Tokens.ARROW) {
            return pack(ELM_ARROW);
        }
        return EMPTY_KEYS;
    }

}
