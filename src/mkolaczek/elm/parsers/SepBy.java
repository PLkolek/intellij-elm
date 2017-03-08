package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Try;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.core.Sequence.sequence;

public class SepBy {
    public static Parser pipeSep(Parser parser) {
        return sepBy(Tokens.PIPE, parser);
    }

    @NotNull
    public static Parser commaSep(Parser parser) {
        return sepBy(Tokens.COMMA, parser);
    }

    public static Parser tryCommaSep(Parser parser) {
        return Try.tryP(commaSep(parser));
    }

    private static Parser sepBy(Token separator, Parser parser) {
        return sequence(String.format("%s separated list of %ss", separator.getName(), parser.name()),
                parser,
                sepSuffix(separator, parser)
        );
    }

    @NotNull
    public static Many commaSepSuffix(Parser parser) {
        return sepSuffix(Tokens.COMMA, parser);
    }

    @NotNull
    private static Many sepSuffix(Token separator, Parser parser) {
        return Many.many(String.format("more %ss", parser.name()),
                Basic.padded(separator),
                parser
        );
    }
}
