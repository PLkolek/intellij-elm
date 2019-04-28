package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AABBCC implements Parser {
    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        int count = 0;
        while(builder.getTokenType() == Tokens.IF) {
            count++;
            builder.getTokenType() ==
        }


        return null;
    }

    @NotNull
    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        return null;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public String name() {
        return "context sensitive";
    }
}
