package mkolaczek.elm.psi;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;

import java.util.function.Predicate;

public class Tokens {

    public static final Token PORT = new Token("port");
    public static final Token EFFECT = new Token("effect");
    public static final Token MODULE = new Token("module");
    public static final Token IMPORT = new Token("import");
    public static final Token AS = new Token("as");
    public static final Token WHERE = new Token("where");
    public static final Token EXPOSING = new Token("exposing");

    public static final Token OPEN_LISTING = new Token("..");

    public static final Token TYPE = new Token("type");
    public static final Token ALIAS = new Token("alias");

    public static final Token ARROW = new Token("ARROW", "->");
    public static final Token EQUALS = new Token("EQUALS", "=");
    public static final Token PIPE = new Token("PIPE", "|");

    public static final Token BEGIN_COMMENT = new Token("BEGIN_COMMENT", "{-");
    public static final Token BEGIN_DOC_COMMENT = new Token("BEGIN_DOC_COMMENT", "{-|");
    public static final Token END_COMMENT = new Token("END_COMMENT", "-}");

    public static final Token END_DOC_COMMENT = new Token("END_DOC_COMMENT", "-}");

    public static final Token LINE_COMMENT = new Token("LINE_COMMENT", "--");
    public static final Token COMMENT_CONTENT = new Token("COMMENT_CONTENT", "comment content");
    public static final Token LBRACKET = new Token("LBRACKET", "{");

    public static final Token RBRACKET = new Token("RBRACKET", "}");
    public static final Token LPAREN = new Token("LPAREN", "(");
    public static final Token RPAREN = new Token("RPAREN", ")");

    public static final Token INFIXL = new Token("infixl");
    public static final Token INFIXR = new Token("infixr");
    public static final Token INFIX = new Token("infix");
    public static final Token CAP_VAR = new Token("CAP_VAR", "uppercase identifier");
    public static final Token LOW_VAR = new Token("LOW_VAR", "lowercase identifier");
    public static final Token COLON = new Token("COLON", ":");
    public static final Token COMMA = new Token("COMMA", ",");
    public static final Token COMMA_OP = new Token("COMMA_OP", "operator consisting of commas");
    public static final Token DOT = new Token("DOT", ".");
    public static final Token SYM_OP = new Token("SYM_OP", "operator consisting of symbols");
    public static final Token DIGIT = new Token("digit");


    public static final Token RUNE_OF_AUTOCOMPLETION = new Token("RUNE_OF_AUTOCOMPLETION");

    public static final TokenSet KEY_TOKENS = TokenSet.create(CAP_VAR, LOW_VAR, SYM_OP);
    public static final TokenSet COMMENT_TOKENS = TokenSet.create(COMMENT_CONTENT, BEGIN_COMMENT, END_COMMENT,
            LINE_COMMENT);

    public static boolean is(Token compared, Token... expected) {
        return Sets.newHashSet(expected).contains(compared);
    }

    public static Predicate<PsiBuilder> is(Token... expected) {
        return builder -> is((Token) builder.getTokenType(), expected);
    }

}
