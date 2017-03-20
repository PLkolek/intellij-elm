package mkolaczek.elm.psi;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;

import java.util.function.Predicate;

public interface Tokens {

    Token PORT = new Token("port");
    Token EFFECT = new Token("effect");
    Token MODULE = new Token("module");
    Token IMPORT = new Token("import");
    Token AS = new Token("as");
    Token WHERE = new Token("where");
    Token EXPOSING = new Token("exposing");
    Token TYPE = new Token("type");
    Token ALIAS = new Token("alias");
    Token INFIXL = new Token("infixl");
    Token INFIXR = new Token("infixr");
    Token INFIX = new Token("infix");

    Token OPEN_LISTING = new Token("..");
    Token ARROW = new Token("ARROW", "->");
    Token EQUALS = new Token("EQUALS", "=");
    Token PIPE = new Token("PIPE", "|");
    Token COLON = new Token("COLON", ":");
    Token COMMA = new Token("COMMA", ",");
    Token DOT = new Token("DOT", ".");
    Token CONS = new Token("CONS", "::");
    Token UNDERSCORE = new Token("UNDERSCORE", "_");
    Token MULTILINE_STRING = new Token("MULTILINE_STRING", "\"\"\"");
    Token QUOTE = new Token("QUOTE", "\"");
    Token SINGLE_QUOTE = new Token("SINGLE_QUOTE", "\'");

    Token BEGIN_COMMENT = new Token("BEGIN_COMMENT", "{-");
    Token BEGIN_DOC_COMMENT = new Token("BEGIN_DOC_COMMENT", "{-|");
    Token END_COMMENT = new Token("END_COMMENT", "-}");
    Token END_DOC_COMMENT = new Token("END_DOC_COMMENT", "-}");
    Token LINE_COMMENT = new Token("LINE_COMMENT", "--");
    Token COMMENT_CONTENT = new Token("COMMENT_CONTENT", "comment content");

    Token STRING_CONTENT = new Token("STRING_CONTENT", "string content");
    Token INVALID_EOL_IN_STRING = new Token("INVALID_EOL_IN_STRING", "invalid end of line in single line string");
    //Those should be StringEscapesTokenTypes for something to work (ie. ToUppercase), but...
    Token VALID_STRING_ESCAPE_TOKEN = new Token("VALID_STRING_ESCAPE_TOKEN", "valid escape sequence");
    Token INVALID_CHARACTER_ESCAPE_TOKEN = new Token("INVALID_CHARACTER_ESCAPE_TOKEN", "invalid escape sequence");
    Token INVALID_UNICODE_ESCAPE_TOKEN = new Token("INVALID_UNICODE_ESCAPE_TOKEN", "invalid unicode escape sequence");


    Token LBRACKET = new Token("LBRACKET", "{");
    Token RBRACKET = new Token("RBRACKET", "}");
    Token LPAREN = new Token("LPAREN", "(");
    Token RPAREN = new Token("RPAREN", ")");
    Token LSQUAREBRACKET = new Token("LSQUAREBRACKET", "[");
    Token RSQUAREBRACKET = new Token("RSQUAREBRACKET", "]");

    Token CAP_VAR = new Token("CAP_VAR", "uppercase identifier");
    Token LOW_VAR = new Token("LOW_VAR", "lowercase identifier");
    Token SYM_OP = new Token("SYM_OP", "operator consisting of symbols");

    Token DIGIT = new Token("digit");
    Token RUNE_OF_AUTOCOMPLETION = new Token("RUNE_OF_AUTOCOMPLETION");
    TokenSet KEY_TOKENS = TokenSet.create(CAP_VAR, LOW_VAR, SYM_OP);
    TokenSet COMMENT_TOKENS = TokenSet.create(COMMENT_CONTENT, BEGIN_COMMENT, END_COMMENT,
            LINE_COMMENT);

    static boolean is(Token compared, Token... expected) {
        return Sets.newHashSet(expected).contains(compared);
    }

    static Predicate<PsiBuilder> is(Token... expected) {
        return builder -> is((Token) builder.getTokenType(), expected);
    }

}
