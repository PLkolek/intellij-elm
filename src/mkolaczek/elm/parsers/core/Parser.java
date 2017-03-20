package mkolaczek.elm.parsers.core;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Token;

import java.util.Collection;
import java.util.HashSet;

import static java.util.Arrays.stream;

public interface Parser {

    boolean parse(PsiBuilder psiBuilder, Collection<Parser> nextParsers);

    boolean willParse(PsiBuilder psiBuilder);

    boolean isRequired();

    String name();

    default Parser as(Element as) {
        return new As(this, as, As.Mode.SKIP_EMPTY);
    }

    default Parser as(Element as, As.Mode mode) {
        return new As(this, as, mode);
    }

    default Parser ll2(HashSet<Token> firstTokens, HashSet<Token> secondTokens) {
        return new LL2(this, firstTokens, secondTokens);
    }

    static boolean anyRequired(Parser... parsers) {
        return stream(parsers).anyMatch(Parser::isRequired);
    }

    static boolean allRequired(Parser... parsers) {
        return stream(parsers).allMatch(Parser::isRequired);
    }
}
