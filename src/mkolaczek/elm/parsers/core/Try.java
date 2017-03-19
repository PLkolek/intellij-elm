package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Token;

import java.util.Optional;
import java.util.Set;

import static mkolaczek.elm.parsers.core.SkipUntil.nextValid;

public class Try implements Parser {

    private final Parser contents;

    public static Parser tryP(Parser contents) {
        return new Try(contents);
    }

    public Try(Parser contents) {
        this.contents = contents;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean parse(PsiBuilder builder, Set<Token> myNextTokens) {
        Set<Token> nextTokens = Sets.union(myNextTokens, startingTokens());
        IElementType token = builder.getTokenType();
        if (startingTokens().contains(token)) {
            contents.parse(builder, myNextTokens);
        } else if (!myNextTokens.contains(token)) {
            Optional<Token> nextValid = nextValid(nextTokens, builder);
            if (nextValid.isPresent() && startingTokens().contains(nextValid.get())) {
                SkipUntil.skipUntil(name(), startingTokens(), builder);
                contents.parse(builder, myNextTokens);
            }
        }
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return contents.startingTokens();
    }

    @Override
    public Set<Token> secondTokens() {
        return contents.secondTokens();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return contents.name();
    }
}
