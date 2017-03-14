package mkolaczek.elm.parsers.core;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static mkolaczek.elm.psi.Tokens.CAP_VAR;
import static mkolaczek.elm.psi.Tokens.RUNE_OF_AUTOCOMPLETION;

public class DottedCapVar implements Parser {

    private final String name;
    @Nullable
    private final Element prefixType;
    @Nullable
    private final Element suffixType;

    public DottedCapVar(String name, @Nullable Element prefixType, @Nullable Element suffixType) {
        this.name = name;
        this.prefixType = prefixType;
        this.suffixType = suffixType;
    }

    public static DottedCapVar dottedCapVar(String name) {
        return new DottedCapVar(name, null, null);
    }

    public static DottedCapVar dottedCapVar(String name, @NotNull Element prefix, @NotNull Element suffix) {
        return new DottedCapVar(name, prefix, suffix);
    }

    @Override
    public boolean parse(PsiBuilder builder, Set<Token> nextTokens) {
        if (builder.eof() || !isCapVar(builder)) {
            return false;
        }
        PsiBuilder.Marker prefix = builder.mark();
        PsiBuilder.Marker prefixEnd = builder.mark();
        PsiBuilder.Marker suffix = builder.mark();
        builder.advanceLexer();
        int start = builder.getCurrentOffset();
        while (nextPartCorrect(builder)) {
            prefixEnd = replace(builder, prefixEnd);
            builder.advanceLexer();
            suffix = replace(builder, suffix);
            builder.advanceLexer();
        }
        if (suffixType != null) {
            suffix.done(suffixType);
        } else {
            suffix.drop();
        }
        if (start != builder.getCurrentOffset() && prefixType != null) {
            prefix.doneBefore(prefixType, prefixEnd);
        } else {
            prefix.drop();
        }
        prefixEnd.drop();
        return true;
    }

    @NotNull
    private PsiBuilder.Marker replace(PsiBuilder builder, PsiBuilder.Marker prefixEnd) {
        prefixEnd.drop();
        prefixEnd = builder.mark();
        return prefixEnd;
    }

    private boolean nextPartCorrect(PsiBuilder builder) {
        PsiBuilder.Marker start = builder.mark();
        boolean result = false;
        if (builder.getTokenType() == Tokens.DOT && WhiteSpace.Type.NO.accepts(builder)) {
            builder.advanceLexer();
            if (isCapVar(builder) && WhiteSpace.Type.NO.accepts(builder)) {
                result = true;
            }
        }
        start.rollbackTo();
        return result;
    }

    private boolean isCapVar(PsiBuilder builder) {
        return builder.getTokenType() == CAP_VAR || builder.getTokenType() == RUNE_OF_AUTOCOMPLETION;
    }

    @Override
    public Set<Token> startingTokens() {
        return ImmutableSet.of(Tokens.CAP_VAR, Tokens.RUNE_OF_AUTOCOMPLETION);
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
