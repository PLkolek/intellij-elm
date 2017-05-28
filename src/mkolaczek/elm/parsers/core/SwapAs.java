package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import mkolaczek.elm.psi.Element;

import java.util.Collection;

public class SwapAs implements Parser {

    private final Parser content;
    private final Element as;

    public SwapAs(Parser content, Element as) {
        this.content = content;
        this.as = as;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        Result result = content.parse(builder, nextParsers, context);
        if (result != Result.TOKEN_ERROR) {
            context.getAsTypes().swap(as);
        }

        return result;
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        return content.willParse(psiBuilder, indentation, lookahead);
    }

    @Override
    public boolean isRequired() {
        return content.isRequired();
    }

    @Override
    public String name() {
        return content.name();
    }
}
