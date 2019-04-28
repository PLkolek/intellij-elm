package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Context;
import mkolaczek.elm.parsers.core.context.Indentation;
import mkolaczek.elm.parsers.core.context.WillParseResult;

import java.util.Collection;
import java.util.List;

public class Sequence implements Parser {

    private final String name;
    private final Parser[] parsers;

    public static Sequence sequence(String name, Parser... parsers) {
        return new Sequence(name, parsers);
    }

    public static Sequence sequence(Parser... parsers) {
        return new Sequence(name(parsers), parsers);
    }

    private static String name(Parser[] parsers) {
        Preconditions.checkArgument(parsers.length > 0);
        for (Parser parser : parsers) {
            if (parser.isRequired()) {
                return parser.name();
            }
        }
        return "Something is wrong, that name should never be needed!";
    }

    private Sequence(String name, Parser... parsers) {
        this.name = name;
        this.parsers = parsers;
    }

    private List<Collection<Parser>> nextParsers(Collection<Parser> myNextParsers) {
        List<Collection<Parser>> result = Lists.newArrayList();
        Collection<Parser> nextParsers = Lists.newArrayList(myNextParsers);
        for (int i = parsers.length - 1; i >= 0; i--) {
            result.add(Lists.newArrayList(nextParsers));
            nextParsers.add(parsers[i]);
        }
        return Lists.reverse(result);
    }

    @Override
    public WillParseResult willParse(PsiBuilder psiBuilder, Indentation indentation, int lookahead) {
        PsiBuilder.Marker marker = psiBuilder.mark();
        for (Parser parser : parsers) {
            WillParseResult parseResult = parser.willParse(psiBuilder, indentation, lookahead);
            if (parseResult.isSuccess()) {
                if (parseResult.remainingLookahead() == 0) {
                    marker.drop();
                    return parseResult;
                }
                lookahead = parseResult.remainingLookahead();
            } else if (parser.isRequired()) {
                marker.rollbackTo();
                return WillParseResult.failure();
            }
        }
        marker.drop();
        return WillParseResult.success(lookahead);
    }

    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> nextParsers, Context context) {
        System.out.println(name);
        List<Collection<Parser>> childrenNextParsers = nextParsers(nextParsers);
        boolean oneParsed = false;
        for (int i = 0; i < parsers.length; i++) {
            Parser parser = parsers[i];
            Collection<Parser> parserNextParsers = childrenNextParsers.get(i);
            Result result = parser.parse(builder, parserNextParsers, context);
            if (result == Result.ERROR) {
                if(!oneParsed) {
                    return Result.ERROR;
                }
                Collection<Parser> skipUntilParsers = Lists.newArrayList(parserNextParsers);
                skipUntilParsers.add(parser);
                SkipUntil.skipUntil(parser.name(), skipUntilParsers, builder, context.getIndentation());
                result = parser.parse(builder, parserNextParsers, context);
            }
            oneParsed = oneParsed || result == Result.OK;
        }
        return oneParsed ? Result.OK : Result.ERROR;
    }

    @Override
    public boolean isRequired() {
        return Parser.anyRequired(parsers);
    }

    @Override
    public String name() {
        return name;
    }
}
