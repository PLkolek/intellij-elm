package mkolaczek.elm.parsers;

import mkolaczek.elm.parsers.core.Many;
import mkolaczek.elm.parsers.core.Parser;
import mkolaczek.elm.parsers.core.Try;
import mkolaczek.elm.psi.Element;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.core.Expect.expect;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.WhiteSpace.maybeWhitespace;

public class SepBy {
    public static Parser pipeSep(Parser parser) {
        return sepBy(Elements.PIPE_SEP, Tokens.PIPE, parser);
    }

    @NotNull
    public static Parser commaSep(Parser parser) {
        return sepBy(Elements.COMMA_SEP, Tokens.COMMA, parser);
    }

    public static Parser tryCommaSep(Parser parser) {
        return Try.tryP(commaSep(parser));
    }

    private static Parser sepBy(Element as, Token separator, Parser parser) {
        return sequence(String.format("%s separated list of %ss", separator.getName(), parser.name()),
                parser,
                sepSuffix(separator, parser)
        ).as(as);
    }

    @NotNull
    public static Many commaSepSuffix(Parser parser) {
        return sepSuffix(Tokens.COMMA, parser);
    }

    @NotNull
    private static Many sepSuffix(Token separator, Parser parser) {
        return Many.many(String.format("more %ss", parser.name()),
                maybeWhitespace(expect(separator)),
                maybeWhitespace(parser)
        );
    }
}
