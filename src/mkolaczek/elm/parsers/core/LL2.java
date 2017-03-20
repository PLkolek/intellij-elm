package mkolaczek.elm.parsers.core;


import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Collection;
import java.util.Set;

//temprarily (?) ugly
public class LL2 implements Parser {

    private final Parser contents;
    private final Set<Token> firstTokens;
    private final Set<Token> secondTokens;

    public LL2(Parser contents, Set<Token> firstTokens, Set<Token> secondTokens) {
        this.contents = contents;
        this.firstTokens = firstTokens;
        this.secondTokens = secondTokens;
    }

    @Override
    public boolean parse(PsiBuilder builder, Collection<Parser> nextParsers) {
        return willParse(builder) && contents.parse(builder, nextParsers);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean willParse(PsiBuilder builder) {
        return !builder.eof()
                && firstTokens.contains(builder.getTokenType())
                && secondTokens.contains(builder.lookAhead(1));
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
