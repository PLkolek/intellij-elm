package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * When parsing whitespace, errors are inserted into AST, but ignored in whole parsing.
 * I don't want to screw everything just because somebody forgot to indent a line.
 */
public class WhiteSpace implements Parser {

    public static boolean isFreshLine(@NotNull PsiBuilder builder) {
        return builder.getCurrentOffset() <= 0 || builder.getOriginalText()
                                                         .charAt(builder.getCurrentOffset() - 1) == '\n';
    }

    public enum Type {
        MAYBE("Whitespace") {
            @Override
            boolean accepts(PsiBuilder builder) {
                return lastChar(builder) != '\n';
            }
        },
        FORCED("Forced WS") {
            @Override
            boolean accepts(PsiBuilder builder) {
                return lastChar(builder) == ' ' || lastChar(builder) == '\t';
            }
        }, FRESH_LINE("Fresh line") {
            @Override
            boolean accepts(PsiBuilder builder) {
                return isFreshLine(builder);
            }
        }, NO("No whitespace") {
            @Override
            boolean accepts(PsiBuilder builder) {
                return !Character.isWhitespace(lastChar(builder));
            }
        };

        private final String name;

        Type(String name) {
            this.name = name;
        }

        abstract boolean accepts(PsiBuilder builder);
    }

    private final Type type;

    public static WhiteSpace maybeWhitespace() {
        return new WhiteSpace(Type.MAYBE);
    }

    public static WhiteSpace freshLine() {
        return new WhiteSpace(Type.FRESH_LINE);
    }

    public static WhiteSpace forcedWhitespace() {
        return new WhiteSpace(Type.FORCED);
    }

    public static WhiteSpace noWhitespace() {
        return new WhiteSpace(Type.NO);
    }

    private WhiteSpace(Type type) {
        this.type = type;
    }

    @Override
    public boolean parse(PsiBuilder builder, Set<Token> nextTokens) {
        if (!type.accepts(builder)) {
            error(builder);
        }
        return true;
    }

    private static char lastChar(PsiBuilder builder) {
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
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return type.name;
    }

}
