package mkolaczek.elm.parsers.faultTolerant;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Or extends FTParserAbstr {

    private final FTParser[] parsers;

    public static Or or(String name, FTParser... parsers) {
        return new Or(name, parsers);
    }

    private Or(String name, FTParser... parsers) {
        super(name, startingTokens(parsers), false, null);
        this.parsers = parsers;
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        for (FTParser parser : parsers) {
            if (parser.parse(builder)) {
                return;
            }
        }
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        for (FTParser parser : parsers) {
            parser.computeNextTokens(myNextTokens);
        }
    }


    private static Set<Token> startingTokens(FTParser... parsers) {
        Set<Token> result = Sets.newHashSet();
        for (FTParser parser : parsers) {
            result.addAll(parser.startingTokens());
        }
        return result;
    }
}
