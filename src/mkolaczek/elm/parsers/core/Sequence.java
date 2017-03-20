package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

    public Sequence separatedBy(Function<Parser, WhiteSpace2> whiteSpace) {
        Parser[] parsers = Arrays.stream(this.parsers).map(whiteSpace).toArray(Parser[]::new);
        parsers[0] = this.parsers[0]; //it's separated, not prefixed
        return new Sequence(name, parsers);
    }

    public Sequence separatedBy(WhiteSpace whiteSpace) {
        Parser[] newParsers = new Parser[parsers.length * 2 - 1];
        for (int i = 0; i < parsers.length; i++) {
            newParsers[2 * i] = parsers[i];
            if (i > 0) {
                newParsers[2 * i - 1] = whiteSpace;
            }
        }

        return new Sequence(name, newParsers);
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
    public boolean willParse(PsiBuilder psiBuilder) {
        for (Parser parser : parsers) {
            if (parser.willParse(psiBuilder)) {
                return true;
            }
            if (parser.isRequired()) {
                return false;
            }
        }
        return false;
    }

    @Override
    public Result parse(PsiBuilder psiBuilder, Collection<Parser> nextParsers) {
        if (psiBuilder.eof() || !willParse(psiBuilder)) {
            return Result.TOKEN_ERROR;
        }
        //noinspection SuspiciousMethodCalls
        return parse2(psiBuilder, nextParsers);
    }

    public Result parse2(PsiBuilder psiBuilder, Collection<Parser> nextParsers) {
        List<Collection<Parser>> childrenNextParsers = nextParsers(nextParsers);

        for (int i = 0; i < parsers.length; i++) {
            Parser parser = parsers[i];
            Collection<Parser> parserNextParsers = childrenNextParsers.get(i);
            Result result = parser.parse(psiBuilder, parserNextParsers);
            if (result == Result.WS_ERROR) {
                return result;
            } else if (result == Result.TOKEN_ERROR) {
                Collection<Parser> skipUntilParsers = Lists.newArrayList(parserNextParsers);
                skipUntilParsers.add(parser);
                SkipUntil.skipUntil(parser.name(), skipUntilParsers, psiBuilder);
                parser.parse(psiBuilder, parserNextParsers);
            }
        }
        return Result.OK;
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
