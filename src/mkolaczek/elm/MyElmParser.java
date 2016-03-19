package mkolaczek.elm;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.parsers.Basic;
import mkolaczek.elm.parsers.Parser;
import mkolaczek.elm.parsers.Whitespace;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;

public class MyElmParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        Marker marker = builder.mark();
        module(builder);
        consumeRest(builder);
        marker.done(root);
        return builder.getTreeBuilt();
    }

    private boolean module(@NotNull PsiBuilder builder) {
        Marker marker = builder.mark();
        boolean success = Basic.sequence(builder,
                Basic.expect(ElmTypes.MODULE),
                Whitespace::maybeWhitespace,
                this::dottedCapVar,
                MyElmParser.listing(exportValue()),
                Whitespace::maybeWhitespace,
                Basic.expect(ElmTypes.WHERE),
                Whitespace::freshLine
        );
        if (!success) {
            consumeLine(builder);
        }
        marker.done(ElmTypes.MODULE_HEADER);
        return success;
    }


    private static Parser listing(Parser listedValue) {
        return (PsiBuilder builder) -> {
            Marker m = builder.mark();
            Whitespace.maybeWhitespace(builder);
            if (builder.getTokenType() != ElmTypes.LPAREN) {
                m.rollbackTo();
                return true;
            }
            boolean success = Basic.sequence(builder,
                    Basic.expect(ElmTypes.LPAREN),
                    Whitespace::maybeWhitespace,
                    MyElmParser.listingContent(listedValue),
                    Whitespace::maybeWhitespace,
                    Basic.expect(ElmTypes.RPAREN)
            );
            m.done(ElmTypes.MODULE_VALUE_LIST);
            return success;
        };
    }

    private static Parser listingContent(Parser listedValue) {
        return (PsiBuilder builder) ->
                Basic.or(builder,
                        Basic.expect(ElmTypes.OPEN_LISTING),
                        MyElmParser.listingValues(listedValue));
    }

    private static Parser listingValues(Parser listedValue) {
        return (builder) -> listedValue.apply(builder) && Basic.many(builder, paddedComma(), listedValue);
    }

    private static Parser paddedComma() {
        return (builder) -> {
            Marker marker = builder.mark();
            Whitespace.maybeWhitespace(builder);
            if (builder.getTokenType() != ElmTypes.COMMA) {
                marker.rollbackTo();
                return false;
            }
            marker.drop();
            return Basic.sequence(builder,
                    Whitespace::maybeWhitespace,
                    Basic.expect(ElmTypes.COMMA),
                    Whitespace::maybeWhitespace);
        };
    }

    private static Parser exportValue() {
        return (PsiBuilder builder) ->
                Basic.or(builder,
                        Basic.expect(ElmTypes.LOW_VAR),
                        MyElmParser::operator,
                        MyElmParser::typeExport
                );
    }

    private static boolean typeExport(PsiBuilder builder) {
        return Basic.sequence(builder,
                Basic.expect(ElmTypes.CAP_VAR),
                listing(Basic.expect(ElmTypes.CAP_VAR)));
    }

    private static boolean operator(PsiBuilder builder) {
        return Basic.sequence(builder,
                Basic.expect(ElmTypes.LPAREN),
                Whitespace::maybeWhitespace,
                MyElmParser::operatorSymbol,
                Whitespace::maybeWhitespace,
                Basic.expect(ElmTypes.RPAREN)
        );
    }

    private static boolean operatorSymbol(PsiBuilder builder) {
        return Basic.simpleOr(builder, ElmTypes.SYM_OP, ElmTypes.COMMA_OP);
    }

    private boolean dottedCapVar(@NotNull PsiBuilder builder) {
        Marker m = builder.mark();
        boolean success = Basic.sequence(builder,
                Basic.expect(ElmTypes.CAP_VAR),
                Basic.simpleMany(ElmTypes.DOT, ElmTypes.CAP_VAR)
        );
        m.done(ElmTypes.DOTTED_CAP_VAR);
        return success;
    }

    private void consumeLine(PsiBuilder builder) {
        while (!builder.eof() && builder.getTokenType() != ElmTypes.NEW_LINE) {
            builder.advanceLexer();
        }
    }

    private void consumeRest(PsiBuilder builder) {
        while (!builder.eof()) {
            builder.advanceLexer();
        }
    }
}
