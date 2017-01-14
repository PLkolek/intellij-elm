package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

public class Try extends FTParserAbstr {

    private final FTParser contents;

    public static FTParser tryP(FTParser contents) {
        return new Try(contents);
    }

    public Try(FTParser contents) {
        super(contents.name(), contents.startingTokens(), true, null);
        this.contents = contents;
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        contents.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        contents.computeNextTokens(myNextTokens);
    }
}
