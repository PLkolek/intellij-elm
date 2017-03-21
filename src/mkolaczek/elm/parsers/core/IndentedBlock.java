package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.IndentationUtil;

import java.util.Collection;

public class IndentedBlock implements Parser {

    private final Parser contents;

    public static IndentedBlock indentedBlock(Parser contents) {
        return new IndentedBlock(contents);
    }

    private IndentedBlock(Parser contents) {
        this.contents = contents;
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Indentation indentation) {
        indentation.push(IndentationUtil.column(builder));
        Result result = contents.parse(builder, nextParsers, indentation);
        indentation.pop();
        return result;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder, Indentation indentation) {
        return contents.willParse(psiBuilder, indentation);
    }

    @Override
    public boolean isRequired() {
        return contents.isRequired();
    }

    @Override
    public String name() {
        return contents.name();
    }
}
