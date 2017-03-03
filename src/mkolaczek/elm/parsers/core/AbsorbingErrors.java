package mkolaczek.elm.parsers.core;

import com.google.common.collect.Sets;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class AbsorbingErrors implements Parser {

    private final Parser content;

    public AbsorbingErrors(Parser content) {
        this.content = content;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean parse(PsiBuilder builder, Set<Token> myNextTokens) {
        Set<Token> nextTokens = Sets.union(myNextTokens, startingTokens());
        IElementType token = builder.getTokenType();
        if (startingTokens().contains(token)) {
            content.parse(builder, myNextTokens);
        } else if (!myNextTokens.contains(token)) {
            //^ absorbing errors is used for absorbing the runic utf character
            //so if we are going to parse the next parser in a moment, there is no need to insert an error
            SkipUntil.skipUntil(name(), nextTokens, builder);
            if (startingTokens().contains(token)) {
                content.parse(builder, myNextTokens);
            }
        }
        return true;
    }

    @Override
    public Set<Token> startingTokens() {
        return content.startingTokens();
    }

    @Override
    public boolean isRequired() {
        return content.isRequired();
    }

    @Override
    public String name() {
        return content.name();
    }
}
