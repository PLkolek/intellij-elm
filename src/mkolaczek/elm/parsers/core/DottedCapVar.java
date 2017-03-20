package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.features.autocompletion.ElmCompletionContributor;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

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
    public boolean willParse(PsiBuilder psiBuilder) {
        return isCapVar(psiBuilder);
    }

    @Override
    public boolean parse(PsiBuilder builder, Collection<Parser> nextParsers) {
        if (builder.eof() || !isCapVar(builder)) {
            return false;
        }
        PsiBuilder.Marker prefix = builder.mark();
        PsiBuilder.Marker prefixEnd = builder.mark();
        PsiBuilder.Marker suffix = builder.mark();
        int suffixStart = builder.getCurrentOffset();
        builder.advanceLexer();
        int start = builder.getCurrentOffset();
        while (isDot(builder) && WhiteSpace.Type.NO.accepts(builder)) {
            prefixEnd = replace(builder, prefixEnd);
            builder.advanceLexer();
            suffix = replace(builder, suffix);
            suffixStart = builder.getCurrentOffset();
            if (!isCapVar(builder) || !WhiteSpace.Type.NO.accepts(builder)) {
                builder.error("Uppercase identifier expected");
                break;
            }
            builder.advanceLexer();
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
        return true;
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

    private boolean isCapVar(PsiBuilder builder) {
        return builder.getTokenType() == CAP_VAR || builder.getTokenType() == RUNE_OF_AUTOCOMPLETION;
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
