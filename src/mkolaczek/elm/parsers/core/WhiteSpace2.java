package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;

import java.util.Collection;

public class WhiteSpace2 implements Parser {

    private final Parser prefixedParsed;
    private final WhiteSpace.Type type;
    private final boolean skipWsError;

    public static WhiteSpace2 maybeWhitespace(Parser prefixedParsed) {
        return new WhiteSpace2(prefixedParsed, WhiteSpace.Type.MAYBE, false);
    }

    public WhiteSpace2(Parser prefixedParsed, WhiteSpace.Type type, boolean skipWsError) {
        this.prefixedParsed = prefixedParsed;
        this.type = type;
        this.skipWsError = skipWsError;
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
}
