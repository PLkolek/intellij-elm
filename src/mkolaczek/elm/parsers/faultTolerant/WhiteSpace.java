package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.Whitespace;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;

import java.util.Set;

/**
 * When parsing whitespace, errors are inserted into AST, but ignored in whole parsing.
 * I don't want to screw everything just because somebody forgot to indent a line.
 */
public class WhiteSpace implements FTParser {

    public enum Type {
        MAYBE, FORCED, FRESH_LINE, NO
    }

    private Set<Token> nextTokens;
    private final Type type;

    public WhiteSpace(Type type) {
        this.type = type;
    }

    @Override
    public boolean parse(PsiBuilder builder) {
        switch (type) {
            case NO:
                if (Character.isWhitespace(lastChar(builder))) {
                    error(builder);
                }
                break;
            case MAYBE:
                if (lastChar(builder) == '\n') {
                    error(builder);
                }
                break;
            case FRESH_LINE:
                if (!Whitespace.isFreshLine(builder)) {
                    error(builder);
                }
                break;
            case FORCED:
                char lastChar = lastChar(builder);
                if (lastChar != ' ' && lastChar != '\t') {
                    error(builder);
                }
                break;
        }
        return true;
    }

    private char lastChar(PsiBuilder builder) {
        return builder.getOriginalText().charAt(builder.getCurrentOffset() - 1);
    }

    private void error(PsiBuilder builder) {
        builder.error(name() + " expected");
    }

    @Override
    public Set<Token> startingTokens() {
        return Sets.newHashSet(Tokens.BEGIN_COMMENT);
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public String name() {
        switch (type) {
            case MAYBE:
                return "Whitespace";
            case FORCED:
                return "Forced WS";
            case FRESH_LINE:
                return "Fresh line";
            case NO:
                return "No whitespace";
        }
        throw new IllegalStateException("Unsupported type: " + type);
    }

    @Override
    public Set<Token> nextTokens() {
        return nextTokens;
    }

    @Override
    public void computeNextTokens(Set<Token> myNextTokens) {
        this.nextTokens = myNextTokens;
    }
}
