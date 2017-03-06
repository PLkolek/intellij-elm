package mkolaczek.elm.parsers.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

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

    private List<Set<Token>> nextTokens2(Set<Token> myNextTokens) {
        //if a parser fails, we might either skip tokens until it succeeds, or until the next parser succeeds
        //hence, next tokes for each parser contain next parsers' tokens and its starting tokens
        List<Set<Token>> result = Lists.newArrayList();
        Set<Token> nextTokens = Sets.newHashSet(myNextTokens);
        for (int i = parsers.length - 1; i >= 0; i--) {
            nextTokens.addAll(parsers[i].startingTokens());
            result.add(Sets.newHashSet(nextTokens));
        }
        return Lists.reverse(result);
    }

    @NotNull
    private static Set<Token> startingTokens(Parser... parsers) {
        Set<Token> startingTokens = Sets.newHashSet();
        for (Parser parser : parsers) {
            startingTokens.addAll(parser.startingTokens());
            if (parser.isRequired()) {
                break;
            }
        }
        return startingTokens;
    }

    @Override
    public boolean parse(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        //noinspection SuspiciousMethodCalls
        if (psiBuilder.eof() || !startingTokens().contains(psiBuilder.getTokenType())) {
            return false;
        }

        parse2(psiBuilder, nextTokens);
        return true;
    }

    public void parse2(PsiBuilder psiBuilder, Set<Token> nextTokens) {
        List<Set<Token>> childrenNextTokens = nextTokens2(nextTokens);

        for (int i = 0; i < parsers.length; i++) {
            Parser parser = parsers[i];
            Set<Token> parserNextTokens = childrenNextTokens.get(i);
            if (!parser.parse(psiBuilder, parserNextTokens)) {
                SkipUntil.skipUntil(parser.name(), parserNextTokens, psiBuilder);
                parser.parse(psiBuilder, parserNextTokens);
            }
        }
    }

    @Override
    public Set<Token> startingTokens() {
        return startingTokens(parsers);
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
