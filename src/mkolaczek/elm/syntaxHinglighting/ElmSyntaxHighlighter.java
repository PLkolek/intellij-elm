package mkolaczek.elm.syntaxHinglighting;

import com.google.common.collect.Sets;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ElmLexerAdapter;
import mkolaczek.elm.psi.ElmTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ElmSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Set<IElementType> KEYWORDS = Sets.newHashSet(
            ElmTokenTypes.MODULE,
            ElmTokenTypes.AS,
            ElmTokenTypes.EXPOSING,
            ElmTokenTypes.IMPORT,
            ElmTokenTypes.PORT,
            ElmTokenTypes.EFFECT);

    public static final TextAttributesKey KEY =
            createTextAttributesKey("ELM_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("ELM_MULTILINE_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey LINE_COMMENT =
            createTextAttributesKey("ELM_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey DOC_COMMENT =
            createTextAttributesKey("ELM_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);

    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT};
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
            return KEY_KEYS;
        }
        if (tokenType == ElmTokenTypes.COMMENT_CONTENT || tokenType == ElmTokenTypes.COMMENT) {
            return COMMENT_KEYS;
        }
        return EMPTY_KEYS;
    }

}
