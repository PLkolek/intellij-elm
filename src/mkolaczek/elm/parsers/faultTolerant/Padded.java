package mkolaczek.elm.parsers.faultTolerant;

import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.psi.Token;

import java.util.Set;

import static mkolaczek.elm.parsers.faultTolerant.Sequence.sequence;
import static mkolaczek.elm.parsers.faultTolerant.WhiteSpace.maybeWhitespace;

public class Padded extends FTParserAbstr {

    private final Sequence sequence;

    public Padded(FTParser parser) {
        super(parser.name(), parser.startingTokens(), parser.isOptional(), null);
        this.sequence = sequence(parser.name(),
                maybeWhitespace(),
                parser,
                maybeWhitespace()
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
