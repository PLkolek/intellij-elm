package mkolaczek.elm.psi;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;

import java.util.function.Predicate;

public class Tokens {

    public static final Token PORT = new Token("PORT");
    public static final Token EFFECT = new Token("EFFECT");
    public static final Token MODULE = new Token("MODULE_NODE");
    public static final Token IMPORT = new Token("IMPORT");
    public static final Token AS = new Token("AS");
    public static final Token EXPOSING = new Token("EXPOSING");
    public static final Token OPEN_LISTING = new Token("OPEN_LISTING");

    public static final Token ALIAS = new Token("ALIAS");
    public static final Token ARROW = new Token("ARROW");


    public static final Token BEGIN_COMMENT = new Token("BEGIN_COMMENT");
    public static final Token BEGIN_DOC_COMMENT = new Token("BEGIN_DOC_COMMENT");
    public static final Token END_COMMENT = new Token("END_COMMENT");
    public static final Token END_DOC_COMMENT = new Token("END_DOC_COMMENT");
    public static final Token COMMENT = new Token("COMMENT");
    public static final Token COMMENT_CONTENT = new Token("COMMENT_CONTENT");

    public static final Token NEW_LINE = new Token("NEW_LINE");

    public static final Token LBRACKET = new Token("LBRACKET");
    public static final Token RBRACKET = new Token("RBRACKET");
    public static final Token LPAREN = new Token("LPAREN");
    public static final Token RPAREN = new Token("RPAREN");

    public static final Token CAP_VAR = new Token("CAP_VAR");
    public static final Token LOW_VAR = new Token("LOW_VAR");
    public static final Token COLON = new Token("COLON");
    public static final Token COMMA = new Token("COMMA");
    public static final Token COMMA_OP = new Token("COMMA_OP");
    public static final Token DOT = new Token("DOT");
    public static final Token EQUALS = new Token("EQUALS");
    public static final Token OR = new Token("OR");
    public static final Token SYM_OP = new Token("SYM_OP");
    public static final Token TYPE = new Token("TYPE");
    public static final Token WHERE = new Token("WHERE");


    public static final TokenSet KEY_TOKENS = TokenSet.create(CAP_VAR, LOW_VAR);
    public static final TokenSet COMMENT_TOKENS = TokenSet.create(COMMENT_CONTENT, BEGIN_COMMENT, END_COMMENT, COMMENT);

    public static boolean is(Token compared, Token... expected) {
        return Sets.newHashSet(expected).contains(compared);
    }

    public static Predicate<PsiBuilder> is(Token... expected) {
        return builder -> is((Token) builder.getTokenType(), expected);
    }

}
