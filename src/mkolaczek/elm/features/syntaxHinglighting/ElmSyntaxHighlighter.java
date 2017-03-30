package mkolaczek.elm.features.syntaxHinglighting;

import com.google.common.collect.Maps;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import mkolaczek.elm.lexer.ElmLexerAdapter;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

class ElmSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final TokenSet KEYWORDS = TokenSet.create(
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

    private static final TokenSet INVALID_ESCAPES = TokenSet.create(
            Tokens.INVALID_CHARACTER_ESCAPE_TOKEN,
            Tokens.INVALID_UNICODE_ESCAPE_TOKEN
    );

    private static final TokenSet STRING_LITERALS = TokenSet.create(
            Tokens.MULTILINE_STRING,
            Tokens.QUOTE,
            Tokens.STRING_CONTENT
    );

    private static final TokenSet GLSL_TOKENS = TokenSet.create(Tokens.BEGIN_GLSL,
            Tokens.GLSL_CONTENT,
            Tokens.END_GLSL
    );

    private static final TokenSet NUMBERS = TokenSet.create(Tokens.NUMBER, Tokens.INVALID_HEX_NUMBER, Tokens.DIGIT);
    private static final TokenSet PARENTHESES = TokenSet.create(Tokens.LPAREN, Tokens.RPAREN);
    private static final TokenSet BRACES = TokenSet.create(Tokens.LBRACKET, Tokens.RBRACKET);
    private static final TokenSet BRACKETS = TokenSet.create(Tokens.LSQUAREBRACKET, Tokens.RBRACKET);

    private static final TextAttributesKey ELM_ARROW =
            createTextAttributesKey("ELM_ARROW", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    private static final TextAttributesKey ELM_PARENTHESIS =
            createTextAttributesKey("ELM_PARENTHESIS", DefaultLanguageHighlighterColors.PARENTHESES);
    private static final TextAttributesKey ELM_BRACES =
            createTextAttributesKey("ELM_BRACES", DefaultLanguageHighlighterColors.BRACES);
    private static final TextAttributesKey ELM_BRACKETS =
            createTextAttributesKey("ELM_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    private static final TextAttributesKey ELM_COMMA =
            createTextAttributesKey("ELM_COMMA", DefaultLanguageHighlighterColors.COMMA);
    private static final TextAttributesKey ELM_EQ =
            createTextAttributesKey("ELM_EQ", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    private static final TextAttributesKey ELM_PIPE =
            createTextAttributesKey("ELM_PIPE", DefaultLanguageHighlighterColors.OPERATION_SIGN);

    private static final Map<IElementType, TextAttributesKey> coloring = Maps.newHashMap();

    static {
        safeMap(coloring, KEYWORDS, DefaultLanguageHighlighterColors.KEYWORD);
        safeMap(coloring, STRING_LITERALS, DefaultLanguageHighlighterColors.STRING);
        safeMap(coloring, INVALID_ESCAPES, DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
        safeMap(coloring, GLSL_TOKENS, DefaultLanguageHighlighterColors.STRING);
        safeMap(coloring, NUMBERS, DefaultLanguageHighlighterColors.NUMBER);
        safeMap(coloring, PARENTHESES, ELM_PARENTHESIS);
        safeMap(coloring, BRACES, ELM_BRACES);
        safeMap(coloring, BRACKETS, ELM_BRACKETS);
        safeMap(coloring, Tokens.COMMENT_TOKENS, DefaultLanguageHighlighterColors.LINE_COMMENT);
        safeMap(coloring, Tokens.VALID_STRING_ESCAPE_TOKEN, DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
        safeMap(coloring, Tokens.DOT, DefaultLanguageHighlighterColors.DOT);
        safeMap(coloring, Tokens.ARROW, ELM_ARROW);
        safeMap(coloring, Tokens.EQUALS, ELM_EQ);
        safeMap(coloring, Tokens.PIPE, ELM_PIPE);
        safeMap(coloring, Tokens.COMMA, ELM_COMMA);
        safeMap(coloring, TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ElmLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(coloring.get(tokenType));
    }

}
