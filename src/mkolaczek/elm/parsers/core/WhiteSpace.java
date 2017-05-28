package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.IndentationUtil;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class WhiteSpace implements Parser {

    private final Parser prefixedParsed;
    private final Type type;
    private final boolean skipWsError;

    public static WhiteSpace maybeWhitespace(Parser prefixedParsed) {
        return new WhiteSpace(prefixedParsed, Type.MAYBE, false);
    }

    public static WhiteSpace freshLine(Parser prefixedParsed) {
        return new WhiteSpace(prefixedParsed, Type.FRESH_LINE, false);
    }

    public static WhiteSpace noWhiteSpace(Parser prefixedParsed) {
        return new WhiteSpace(prefixedParsed, Type.NO, false);
    }

    public static WhiteSpace indented(Parser prefixedParsed) {
        return new WhiteSpace(prefixedParsed, Type.INDENTED, false);
    }

    private WhiteSpace(Parser prefixedParsed, Type type, boolean skipWsError) {
        this.prefixedParsed = prefixedParsed;
        this.type = type;
        this.skipWsError = skipWsError;
    }

    private static char lastChar(PsiBuilder builder) {
        return builder.getOriginalText().charAt(builder.getCurrentOffset() - 1);
    }

    private static boolean isFreshLine(@NotNull PsiBuilder builder) {
        return builder.getCurrentOffset() <= 0 || builder.getOriginalText()
                                                         .charAt(builder.getCurrentOffset() - 1) == '\n';
    }

    public WhiteSpace skipWsError() {
        return new WhiteSpace(prefixedParsed, type, true);
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        if (!type.accepts(builder, context.getIndentation())) {
            builder.error(type.name + " expected");
            if (!skipWsError) {
                return Result.WS_ERROR;
            }
        }
        return prefixedParsed.parse(builder, nextParsers, context);
    }

    @Override
    public WillParseResult willParse(PsiBuilder builder, Indentation indentation, int lookahead) {
        if (!acceptsWhiteSpace(builder, indentation)) {
            return WillParseResult.failure();
        }
        return prefixedParsed.willParse(builder, indentation, lookahead);
    }

    private boolean acceptsWhiteSpace(PsiBuilder psiBuilder, Indentation indentation) {
        return skipWsError || type.accepts(psiBuilder, indentation);
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
            boolean accepts(PsiBuilder builder, Indentation indentation) {
                if (builder.eof()) {
                    return true;
                }
                Optional<Integer> indent = IndentationUtil.indent(builder);
                boolean correctIndent = !indent.isPresent() || !indentation.contains(indent.get());
                return builder.eof() || (lastChar(builder) != '\n' && correctIndent);
            }
        },
        FRESH_LINE("Fresh line") {
            @Override
            boolean accepts(PsiBuilder builder, Indentation indentation) {
                return isFreshLine(builder);
            }
        }, NO("No whitespace") {
            @Override
            boolean accepts(PsiBuilder builder, Indentation indentation) {
                return !Character.isWhitespace(lastChar(builder));
            }
        }, INDENTED("Indentation") {
            @Override
            boolean accepts(PsiBuilder builder, Indentation indentation) {
                if (builder.eof()) {
                    return false;
                }
                Optional<Integer> indent = IndentationUtil.indent(builder);
                return indent.isPresent() && indentation.current() == indent.get();
            }
        };

        final String name;

        Type(String name) {
            this.name = name;
        }

        abstract boolean accepts(PsiBuilder builder, Indentation indentation);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", type.name, prefixedParsed.name());
    }
}
