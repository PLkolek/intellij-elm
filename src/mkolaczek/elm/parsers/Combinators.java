package mkolaczek.elm.parsers;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.joining;

public class Combinators {

    public static Parser sequence(Parser... parsers) {
        return (builder -> simpleSequence(builder, parsers));
    }

    public static Parser as(IElementType name, Parser parser) {
        return (builder -> {
            Marker marker = builder.mark();
            boolean result = parser.apply(builder);
            marker.done(name);
            return result;
        });
    }

    public static Parser sequenceAs(IElementType name, Parser... parsers) {
        return (builder -> {
            Marker marker = builder.mark();
            boolean result = simpleSequence(builder, parsers);
            endAs(marker, name);
            return result;
        });

    }

    public static boolean simpleSequence(PsiBuilder builder, Parser... parsers) {
        for (Parser parser : parsers) {
            if (!parser.apply(builder)) {
                return false;
            }
        }
        return true;
    }

    public static Parser simpleMany(IElementType... tokens) {
        checkArgument(tokens != null && tokens.length >= 1);
        return (PsiBuilder builder) -> {
            while (builder.getTokenType() == tokens[0]) {
                if (!simpleExpect(builder, tokens)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Parser many(Parser... parsers) {
        return (builder) -> simpleMany(builder, parsers);
    }

    public static boolean simpleMany(PsiBuilder builder, Parser... parsers) {
        return simpleManyAs(builder, null, parsers);
    }

    public static boolean simpleManyAs(PsiBuilder builder, IElementType as, Parser... parsers) {
        checkArgument(parsers != null && parsers.length >= 1);
        Marker marker = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        boolean success;
        do {
            Marker m = builder.mark();
            int offsetBefore = builder.getCurrentOffset();
            success = simpleSequence(builder, parsers);
            if (!success) {
                if (offsetBefore != builder.getCurrentOffset()) {
                    m.drop();
                    marker.drop();
                    return false;
                } else {
                    m.rollbackTo();
                }
            } else {
                m.drop();
            }
        } while (success);
        if (startingOffset != builder.getCurrentOffset()) {
            endAs(marker, as);
        } else {
            marker.drop();
        }
        return true;

    }

    public static NamedParser expectAs(IElementType name, IElementType... tokens) {
        return NamedParser.of(name.toString(), builder -> simpleExpectAs(builder, name, tokens));
    }

    public static NamedParser expect(IElementType... tokens) {
        return NamedParser.of(tokens[0].toString(), builder -> simpleExpect(builder, tokens));
    }

    public static boolean simpleExpect(PsiBuilder builder, IElementType... tokens) {
        return simpleExpectAs(builder, null, tokens);
    }

    public static boolean simpleExpectAs(PsiBuilder builder, @Nullable IElementType name, IElementType... tokens) {
        Marker marker = builder.mark();
        for (IElementType token : tokens) {
            if (builder.getTokenType() != token) {
                builder.error(token.toString() + " expected");
                marker.drop();
                return false;
            }
            builder.advanceLexer();
        }
        endAs(marker, name);
        return true;
    }

    private static void endAs(Marker marker, @Nullable IElementType name) {
        if (name != null) {
            marker.done(name);
        } else {
            marker.drop();
        }
    }

    public static Parser or(NamedParser... parsers) {
        return (builder -> simpleOr(builder, parsers));
    }

    public static Parser orAs(IElementType elementType, NamedParser... parsers) {
        return (builder -> simpleOrAs(builder, elementType, parsers));
    }

    public static Boolean simpleOr(PsiBuilder builder, NamedParser... parsers) {
        return simpleOrAs(builder, null, parsers);
    }

    @NotNull
    private static Boolean simpleOrAs(PsiBuilder builder, IElementType as, NamedParser[] parsers) {
        Marker start = builder.mark();
        for (Parser parser : parsers) {
            int offsetBefore = builder.getCurrentOffset();
            if (parser.apply(builder)) {
                endAs(start, as);
                return true;
            }
            if (offsetBefore != builder.getCurrentOffset()) {
                break;
            } else {
                start.rollbackTo();
            }
            start = builder.mark();
        }
        start.drop();
        String expected = Arrays.stream(parsers).map(NamedParser::name).collect(joining(", "));
        builder.error(expected + " expected in OR");
        return false;
    }

    public static boolean simpleOr(PsiBuilder builder, IElementType... tokens) {
        for (IElementType token : tokens) {
            if (builder.getTokenType() == token) {
                builder.advanceLexer();
                return true;
            }
        }
        builder.error("Someting expected in simpleOR");
        return false;
    }

    public static Parser tryP(Parser parser) {
        return (builder -> simpleTry(builder, parser));
    }

    public static NamedParser tryP(NamedParser parser) {
        return parser.withParser(tryP((Parser) parser));
    }

    public static boolean simpleTry(PsiBuilder builder, Parser parser) {
        Marker marker = builder.mark();
        if (!parser.apply(builder)) {
            marker.rollbackTo();
            return false;
        }
        marker.drop();

        return true;
    }

    @NotNull
    public static Parser spacePrefix(NamedParser parser) {
        return many(tryP(sequence(Whitespace::forcedWS, parser)));
    }

    public static Parser success(Parser parser) {
        return builder -> {
            parser.apply(builder);
            return true;
        };
    }

    public static Parser skipUntil(NamedParser expected, Predicate<PsiBuilder> skipEnd) {
        return builder -> {
            int startOffset = builder.getCurrentOffset();
            Marker errorStart = builder.mark();
            while (!builder.eof()) {
                int endOffset = builder.getCurrentOffset();
                Marker currentMarker = builder.mark();
                if (expected.apply(builder)) {
                    if (endOffset != startOffset) {
                        errorStart.errorBefore(expected.name() + " expected", currentMarker);
                    } else {
                        errorStart.drop();
                    }
                    currentMarker.drop();
                    return true;
                }
                if (skipEnd.test(builder)) {
                    if (endOffset != startOffset) {
                        errorStart.errorBefore(expected.name() + " expected", currentMarker);
                    } else {
                        errorStart.drop();
                        builder.error(expected.name() + " expected");
                    }
                    currentMarker.drop();
                    return false;
                }
                currentMarker.drop();
                builder.advanceLexer();
            }
            int endOffset = builder.getCurrentOffset();
            if (endOffset != startOffset) {
                errorStart.error(expected.name() + " expected");
            } else {
                errorStart.drop();
                builder.error(expected.name() + " expected");
            }
            return false;
        };
    }
}
