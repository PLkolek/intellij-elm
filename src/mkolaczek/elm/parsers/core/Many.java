package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static mkolaczek.elm.parsers.core.IndentedBlock.indentedBlock;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.SkipUntil.*;

public class Many implements Parser {

    private final Parser parser;
    private final String name;

    public static Parser many1(Parser parser) {
        return Sequence.sequence(
                parser,
                many(parser)
        );
    }

    public static Many many(Parser parser) {
        return many(parser.name() + "s", parser);
    }

    public static Many many(String name, Parser parser) {
        return new Many(name, parser);
    }

    public static Many many(String name, Parser... parsers) {
        return new Many(name, Sequence.sequence(name, parsers));
    }

    @NotNull
    public static IndentedBlock indentedMany1(Parser itemParser) {
        return indentedBlock(
                sequence(
                        itemParser,
                        many(itemParser)
                )
        );
    }

    private Many(String name, Parser parser) {
        this.name = name;
        this.parser = parser;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> myNextParsers, Context context) {
        Collection<Parser> childNextParsers = Lists.newArrayList(myNextParsers);
        childNextParsers.add(this);
        do {
            if (willParse(builder, context.getIndentation())) {
                parser.parse(builder, childNextParsers, context);
            } else if (anyWillParse(myNextParsers, builder, context.getIndentation()) || builder.eof()) {
                break;
            } else {
                skipUntil(parser.name(), childNextParsers, builder, context.getIndentation());
            }
        } while (true);
        return Result.OK;
    }

    @NotNull
    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        WillParseResult finalResult = WillParseResult.failure();
        WillParseResult lastResult;
        do {
            lastResult = parser.willParse(psiBuilder, indentation, lookahead);
            if(lastResult.isSuccess()) {
                finalResult = lastResult;
            }
        } while (lastResult.isSuccess() && lastResult.remainingLookahead() > 0);
        return finalResult;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return name;
    }
}
