package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class WhiteSpace2 implements Parser {

    private final Parser prefixedParsed;
    private final Type type;
    private final boolean skipWsError;

    public static WhiteSpace2 maybeWhitespace(Parser prefixedParsed) {
        return new WhiteSpace2(prefixedParsed, Type.MAYBE, false);
    }

    public static WhiteSpace2 freshLine(Parser prefixedParsed) {
        return new WhiteSpace2(prefixedParsed, Type.FRESH_LINE, false);
    }

    public WhiteSpace2(Parser prefixedParsed, Type type, boolean skipWsError) {
        this.prefixedParsed = prefixedParsed;
        this.type = type;
        this.skipWsError = skipWsError;
    }

    private static char lastChar(PsiBuilder builder) {
        return builder.getOriginalText().charAt(builder.getCurrentOffset() - 1);
    }

    public static boolean isFreshLine(@NotNull PsiBuilder builder) {
        return builder.getCurrentOffset() <= 0 || builder.getOriginalText()
                                                         .charAt(builder.getCurrentOffset() - 1) == '\n';
    }

    public WhiteSpace2 skipWsError() {
        return new WhiteSpace2(prefixedParsed, type, true);
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers) {
        if (!type.accepts(builder)) {
            builder.error(type.name + " expected");
            if (!skipWsError) {
                return Result.WS_ERROR;
            }
        }
        if (!prefixedParsed.willParse(builder)) {
            return Result.TOKEN_ERROR;
        }
        return prefixedParsed.parse(builder, nextParsers);
    }

    @Override
    public boolean willParse(PsiBuilder builder) {
        return acceptsWhiteSpace(builder) && prefixedParsed.willParse(builder);
    }

    boolean acceptsWhiteSpace(PsiBuilder psiBuilder) {
        return skipWsError || type.accepts(psiBuilder);
    }

    @Override
    public boolean isRequired() {
        return prefixedParsed.isRequired();
    }

    @Override
    public String name() {
        return prefixedParsed.name();
    }

    public enum Type {
        MAYBE("Whitespace") {
            @Override
            boolean accepts(PsiBuilder builder) {
                return builder.eof() || lastChar(builder) != '\n';
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

        final String name;

        Type(String name) {
            this.name = name;
        }

        abstract boolean accepts(PsiBuilder builder);
    }
}
