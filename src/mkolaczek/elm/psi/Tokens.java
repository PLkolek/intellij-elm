package mkolaczek.elm.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public class Tokens {

    public static final IElementType PORT = new Token("PORT");
    public static final IElementType EFFECT = new Token("EFFECT");
    public static final IElementType MODULE = new Token("MODULE_NODE");
    public static final IElementType IMPORT = new Token("IMPORT");
    public static final IElementType AS = new Token("AS");
    public static final IElementType EXPOSING = new Token("EXPOSING");
    public static final IElementType OPEN_LISTING = new Token("OPEN_LISTING");

    public static final IElementType ALIAS = new Token("ALIAS");
    public static final IElementType ARROW = new Token("ARROW");


    public static final IElementType BEGIN_COMMENT = new Token("BEGIN_COMMENT");
    public static final IElementType BEGIN_DOC_COMMENT = new Token("BEGIN_DOC_COMMENT");
    public static final IElementType END_COMMENT = new Token("END_COMMENT");
    public static final IElementType COMMENT = new Token("COMMENT");
    public static final IElementType COMMENT_CONTENT = new Token("COMMENT_CONTENT");

    public static final IElementType NEW_LINE = new Token("NEW_LINE");

    public static final IElementType LBRACKET = new Token("LBRACKET");
    public static final IElementType RBRACKET = new Token("RBRACKET");
    public static final IElementType LPAREN = new Token("LPAREN");
    public static final IElementType RPAREN = new Token("RPAREN");

    public static final IElementType CAP_VAR = new Token("CAP_VAR");
    public static final IElementType LOW_VAR = new Token("LOW_VAR");
    public static final IElementType COLON = new Token("COLON");
    public static final IElementType COMMA = new Token("COMMA");
    public static final IElementType COMMA_OP = new Token("COMMA_OP");
    public static final IElementType DOT = new Token("DOT");
    public static final IElementType EQUALS = new Token("EQUALS");
    public static final IElementType OR = new Token("OR");
    public static final IElementType SYM_OP = new Token("SYM_OP");
    public static final IElementType TYPE = new Token("TYPE");
    public static final IElementType WHERE = new Token("WHERE");


    public static final TokenSet KEY_TOKENS = TokenSet.create(CAP_VAR, LOW_VAR);
    public static final TokenSet COMMENT_TOKENS = TokenSet.create(COMMENT_CONTENT);


}
