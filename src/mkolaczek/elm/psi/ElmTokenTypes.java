package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public class ElmTokenTypes {

    public static final IElementType PORT = new ElmTokenType("PORT");
    public static final IElementType EFFECT = new ElmTokenType("EFFECT");
    public static final IElementType MODULE = new ElmTokenType("MODULE");
    public static final IElementType IMPORT = new ElmTokenType("IMPORT");
    public static final IElementType AS = new ElmTokenType("AS");
    public static final IElementType EXPOSING = new ElmTokenType("EXPOSING");
    public static final IElementType OPEN_LISTING = new ElmTokenType("OPEN_LISTING");

    public static final IElementType ALIAS = new ElmTokenType("ALIAS");
    public static final IElementType ARROW = new ElmTokenType("ARROW");


    public static final IElementType BEGIN_COMMENT = new ElmTokenType("BEGIN_COMMENT");
    public static final IElementType BEGIN_DOC_COMMENT = new ElmTokenType("BEGIN_DOC_COMMENT");
    public static final IElementType END_COMMENT = new ElmTokenType("END_COMMENT");
    public static final IElementType COMMENT = new ElmTokenType("COMMENT");
    public static final IElementType COMMENT_CONTENT = new ElmTokenType("COMMENT_CONTENT");

    public static final IElementType WHITE_SPACE = new ElmTokenType("WHITE_SPACE");
    public static final IElementType NEW_LINE = new ElmTokenType("NEW_LINE");

    public static final IElementType CAP_VAR = new ElmTokenType("CAP_VAR");
    public static final IElementType COLON = new ElmTokenType("COLON");
    public static final IElementType COMMA = new ElmTokenType("COMMA");
    public static final IElementType COMMA_OP = new ElmTokenType("COMMA_OP");
    public static final IElementType DOT = new ElmTokenType("DOT");
    public static final IElementType EQUALS = new ElmTokenType("EQUALS");
    public static final IElementType LOW_VAR = new ElmTokenType("LOW_VAR");
    public static final IElementType LPAREN = new ElmTokenType("LPAREN");
    public static final IElementType RPAREN = new ElmTokenType("RPAREN");
    public static final IElementType OR = new ElmTokenType("OR");
    public static final IElementType SYM_OP = new ElmTokenType("SYM_OP");
    public static final IElementType TYPE = new ElmTokenType("TYPE");
    public static final IElementType WHERE = new ElmTokenType("WHERE");


    public static final TokenSet KEY_TOKENS = TokenSet.create(CAP_VAR, LOW_VAR);
    public static final TokenSet COMMENT_TOKENS = TokenSet.create(COMMENT_CONTENT);
}
