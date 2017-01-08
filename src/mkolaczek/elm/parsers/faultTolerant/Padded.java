package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.Type.MAYBE;

public class Padded extends FTParserAbstr {

    private final Sequence sequence;

    public Padded(FTParser parser) {
        super(parser.name(), parser.startingTokens(), parser.isOptional(), null);
        this.sequence = new Sequence(parser.name(),
                new WhiteSpace(MAYBE),
                parser,
                new WhiteSpace(MAYBE)
        );
    }

    @Override
    protected void parse2(PsiBuilder builder) {
        this.sequence.parse(builder);
    }

    @Override
    protected void computeNextTokens2(Set<Token> myNextTokens) {
        this.sequence.computeNextTokens(myNextTokens);
    }
}
