package mkolaczek.elm.parsers.core;

import com.google.common.collect.Lists;
import com.intellij.lang.PsiBuilder;
import mkolaczek.elm.parsers.core.context.Indentation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static mkolaczek.elm.parsers.core.IndentedBlock.indentedBlock;
import static mkolaczek.elm.parsers.core.Sequence.sequence;
import static mkolaczek.elm.parsers.core.SkipUntil.*;
import static mkolaczek.elm.parsers.core.WhiteSpace.indented;

public class Many implements Parser {

    private final Parser parser;
    private final String name;

    public static Parser many1(Parser parser) {
        return Sequence.sequence(
                parser,
                many(parser)
        );
    }

    public static Many many(Parser parser) {
        return many(parser.name() + "s", parser);
    }

    public static Many many(String name, Parser parser) {
        return new Many(name, parser);
    }

    public static Many many(String name, Parser... parsers) {
        return new Many(name, Sequence.sequence(name, parsers));
    }

    @NotNull
    public static IndentedBlock indentedMany1(Parser itemParser) {
        return indentedBlock(
                sequence(
                        itemParser,
                        many(indented(itemParser))
                )
        );
    }

    private Many(String name, Parser parser) {
        this.name = name;
        this.parser = parser;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public Result parse(PsiBuilder builder, Collection<Parser> myNextParsers, Indentation indentation) {
        Collection<Parser> childNextParsers = Lists.newArrayList(myNextParsers);
        childNextParsers.add(this);
        do {
            if (willParse(builder, indentation)) {
                parser.parse(builder, childNextParsers, indentation);
            } else if (anyWillParse(myNextParsers, builder, indentation) || builder.eof()) {
                break;
            } else {
                if (willParseAfterSkipping(this, myNextParsers, builder, indentation)) {
                    skipUntil(parser.name(), Lists.newArrayList(this), builder, indentation);
                } else {
                    break;
                }
            }
        } while (true);
        return Result.OK;
    }

    @Override
    public boolean willParse(PsiBuilder psiBuilder, Indentation indentation) {
        return parser.willParse(psiBuilder, indentation);
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String name() {
        return name;
    }
}
