package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Token;

import java.util.Optional;
import java.util.Set;

import static mkolaczek.elm.parsers.core.SkipUntil.nextValid;

public class Try extends ParserAbstr {

    private final Parser contents;

    public static Parser tryP(Parser contents) {
        return new Try(contents);
    }

    public Try(Parser contents) {
        super(contents.name(), true);
        this.contents = contents;
    }

    @Override
    protected void parse2(PsiBuilder builder, Set<Token> myNextTokens) {
        Set<Token> nextTokens = Sets.union(myNextTokens, startingTokens());
        //noinspection SuspiciousMethodCalls
        IElementType token = builder.getTokenType();
        //noinspection SuspiciousMethodCalls
        if (startingTokens().contains(token)) {
            contents.parse(builder, myNextTokens);
        } else //noinspection SuspiciousMethodCalls
            if (!myNextTokens.contains(token)) {
                Optional<Token> nextValid = nextValid(nextTokens, builder);
                if (nextValid.isPresent() && startingTokens().contains(nextValid.get())) {
                    SkipUntil.skipUntil(name(), startingTokens(), builder);
                    contents.parse(builder, myNextTokens);
                }
            }
    }

    @Override
    public Set<Token> startingTokens() {
        return contents.startingTokens();
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
