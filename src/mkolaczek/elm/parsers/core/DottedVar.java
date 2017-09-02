package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static mkolaczek.elm.psi.Elements.MODULE_NAME_REF;
import static mkolaczek.elm.psi.Elements.VAR;
import static mkolaczek.elm.psi.Tokens.*;

public class DottedVar implements Parser {

    private final String name;
    private final Set<Token> varTokens;
    @Nullable
    private final Element prefixType;
    @Nullable
    private final Element suffixType;

    private DottedVar(String name, Set<Token> varTokens, @Nullable Element prefixType, @Nullable Element suffixType) {
        this.name = name;
        this.varTokens = varTokens;
        this.prefixType = prefixType;
        this.suffixType = suffixType;
    }

    public static DottedVar moduleName() {
        return new DottedVar("module name", ImmutableSet.of(CAP_VAR), null, null);
    }

    public static DottedVar qualifiedCapVar(String name, @NotNull Element suffix) {
        return new DottedVar(name, ImmutableSet.of(CAP_VAR), MODULE_NAME_REF, suffix);
    }

    public static Parser qualifiedVar() {
        return new DottedVar("qualified variable", ImmutableSet.of(CAP_VAR, LOW_VAR), MODULE_NAME_REF, VAR);
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        Preconditions.checkArgument(lookahead == 1);
        return WillParseResult.create(isVar(psiBuilder), lookahead - 1);
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        if (builder.eof() || !isVar(builder)) {
            return Result.TOKEN_ERROR;
        }
        PsiBuilder.Marker prefix = builder.mark();
        PsiBuilder.Marker prefixEnd = builder.mark();
        PsiBuilder.Marker suffix = builder.mark();
        int suffixStart = builder.getCurrentOffset();
        IElementType type = builder.getTokenType();
        builder.advanceLexer();
        int start = builder.getCurrentOffset();
        if (type == CAP_VAR) {
            while (isDot(builder)) {
                prefixEnd = replace(builder, prefixEnd);
                builder.advanceLexer();
                suffix = replace(builder, suffix);
                suffixStart = builder.getCurrentOffset();
                if (!isVar(builder)) {
                    builder.error(varTokens + " expected");
                    break;
                }
                type = builder.getTokenType();
                builder.advanceLexer();
                if (type != CAP_VAR) {
                    break;
                }
            }
        }
        if (isValid(builder, suffixType, suffixStart)) {
            suffix.done(suffixType);
        } else {
            suffix.drop();
        }
        if (isValid(builder, prefixType, start)) {
            prefix.doneBefore(prefixType, prefixEnd);
        } else {
            prefix.drop();
        }
        prefixEnd.drop();
        return Result.OK;
    }

    private boolean isDot(PsiBuilder builder) {
        IElementType type = builder.getTokenType();
        String dotAndRune = "." + ElmCompletionContributor.AUTOCOMPLETION_RUNE_SYMBOL;
        return type == Tokens.DOT || type == Tokens.SYM_OP && dotAndRune.equals(builder.getTokenText());
    }

    private boolean isValid(PsiBuilder builder, Element type, int suffixStart) {
        return type != null && suffixStart != builder.getCurrentOffset();
    }

    @NotNull
    private PsiBuilder.Marker replace(PsiBuilder builder, PsiBuilder.Marker prefixEnd) {
        prefixEnd.drop();
        prefixEnd = builder.mark();
        return prefixEnd;
    }

    private boolean isVar(PsiBuilder builder) {
        IElementType tokenType = builder.getTokenType();
        //noinspection SuspiciousMethodCalls
        return tokenType == CAP_VAR || varTokens.contains(tokenType) || tokenType == RUNE_OF_AUTOCOMPLETION;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public String name() {
        return name;
    }
}
