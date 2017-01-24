package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Or extends ParserAbstr {

    private final Parser[] parsers;

    public static Or or(String name, Parser... parsers) {
        return new Or(name, null, parsers);
    }

    public static Or orAs(Element as, Parser... parsers) {
        return new Or(as.getName(), as, parsers);
    }

    private Or(String name, Element as, Parser... parsers) {
        super(name, as);
        this.parsers = parsers;
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        for (Parser parser : parsers) {
            if (parser.parse(builder)) {
                return;
            }
        }
    }


    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        for (Parser parser : parsers) {
            parser.computeNextTokens(myNextTokens);
        }
    }

    private static Set<Token> startingTokens(Parser... parsers) {
        Set<Token> result = Sets.newHashSet();
        for (Parser parser : parsers) {
            result.addAll(parser.startingTokens());
        }
        return result;
    }

    @Override
    public Set<Token> startingTokens() {
        return startingTokens(parsers);
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
