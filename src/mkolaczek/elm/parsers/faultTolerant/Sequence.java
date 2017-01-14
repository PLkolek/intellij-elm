package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Sequence extends FTParserAbstr {

    private final FTParser[] parsers;

    public static Sequence sequence(String name, FTParser... parsers) {
        return new Sequence(name, null, parsers);
    }

    public static Sequence sequenceAs(Element as, FTParser... parsers) {
        return new Sequence(as.getName(), as, parsers);
    }

    public Sequence separatedBy(WhiteSpace whiteSpace) {
        FTParser[] newParsers = new FTParser[parsers.length * 2 - 1];
        for (int i = 0; i < parsers.length; i++) {
            newParsers[2 * i] = parsers[i];
            if (i > 0) {
                newParsers[2 * i - 1] = whiteSpace;
            }
        }

        return new Sequence(name, as, newParsers);
    }

    private Sequence(String name, Element as, FTParser... parsers) {
        super(name, startingTokens(parsers), isOptional(parsers), as);
        this.parsers = parsers;
    }

    @Override
    protected void parse2(PsiBuilder psiBuilder) {
        for (FTParser parser : parsers) {
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
    private static Set<Token> startingTokens(FTParser... parsers) {
        Set<Token> startingTokens = Sets.newHashSet();
        for (FTParser parser : parsers) {
            startingTokens.addAll(parser.startingTokens());
            if (parser.isRequired()) {
                break;
            }
        }
        return startingTokens;
    }

    private static boolean isOptional(FTParser... parsers) {
        for (FTParser parser : parsers) {
            if (parser.isRequired()) {
                return false;
            }
        }
        return true;
    }
}
