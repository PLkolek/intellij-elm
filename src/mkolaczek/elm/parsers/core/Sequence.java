package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Sequence extends ParserAbstr {

    private final Parser[] parsers;

    public static Parser rootSequence(String name, Parser... parsers) {
        return new Sequence(name, null, true, parsers);
    }

    public static Sequence sequence(String name, Parser... parsers) {
        return new Sequence(name, null, parsers);
    }

    public static Sequence sequenceAs(Element as, Parser... parsers) {
        return new Sequence(as.getName(), as, parsers);
    }

    public Sequence separatedBy(WhiteSpace whiteSpace) {
        Parser[] newParsers = new Parser[parsers.length * 2 - 1];
        for (int i = 0; i < parsers.length; i++) {
            newParsers[2 * i] = parsers[i];
            if (i > 0) {
                newParsers[2 * i - 1] = whiteSpace;
            }
        }

        return new Sequence(name, as, newParsers);
    }

    public Sequence(String name, Element as, boolean root, Parser... parsers) {
        super(name, root, as);
        this.parsers = parsers;
    }

    private Sequence(String name, Element as, Parser... parsers) {
        this(name, as, false, parsers);
    }

    @Override
    protected void parse2(PsiBuilder psiBuilder) {
        for (Parser parser : parsers) {
            if (!parser.parse(psiBuilder)) {
                SkipUntil.skipUntil(parser.name(), parser.nextTokens(), psiBuilder);
                parser.parse(psiBuilder);
            }
        }
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        Set<Token> result = Sets.newHashSet(myNextTokens);
        for (int i = parsers.length - 1; i >= 0; i--) {
            parsers[i].computeNextTokens(result);
            result.addAll(parsers[i].startingTokens());

        }
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

    private static boolean isOptional(Parser... parsers) {
        for (Parser parser : parsers) {
            if (parser.isRequired()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return startingTokens(parsers);
    }

    @Override
    public boolean isRequired() {
        return !isOptional(parsers);
    }
}
