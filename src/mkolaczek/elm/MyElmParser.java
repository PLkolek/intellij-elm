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
        if (!builder.eof()) {
            builder.error("EOF expected");
        }
        consumeRest(builder);
        marker.done(root);
        return builder.getTreeBuilt();
    }

    private void module(@NotNull PsiBuilder builder) {
        if (builder.getTokenType() == ElmTypes.MODULE) {
            moduleDeclaration(builder);
        }
        if (builder.getTokenType() == ElmTypes.BEGIN_DOC_COMMENT) {
            docComment(builder);
            Whitespace.freshLine(builder);
        }
        Basic.simpleMany(builder, MyElmParser::importLine);
    }

    private static boolean importLine(PsiBuilder builder) {
        if (builder.getTokenType() != ElmTypes.IMPORT) {
            return false;
        }
        Marker m = builder.mark();
        boolean result = Basic.sequence(builder,
                Basic.expect(ElmTypes.IMPORT),
                Whitespace::maybeWhitespace,
                MyElmParser::dottedCapVar
        );
        if (!result) {
            consumeUntil(builder, ElmTypes.NEW_LINE);
            m.done(ElmTypes.IMPORT_2);
            return false;
        }
        Marker m2 = builder.mark();
        boolean hasAs = Basic.sequence(builder, Whitespace::maybeWhitespace, Basic.expect(ElmTypes.AS));
        if (hasAs) {
            m2.drop();
            if (!Basic.sequence(builder, Whitespace::maybeWhitespace, Basic.expect(ElmTypes.CAP_VAR))) {
                consumeUntil(builder, ElmTypes.NEW_LINE);
                m.done(ElmTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        m2 = builder.mark();
        boolean hasExposing = Basic.sequence(builder, Whitespace::maybeWhitespace, Basic.expect(ElmTypes.EXPOSING));
        if (hasExposing) {
            m2.drop();
            if (!Basic.sequence(builder, Whitespace::maybeWhitespace, listing(MyElmParser::exportValue))) {
                consumeUntil(builder, ElmTypes.NEW_LINE);
                m.done(ElmTypes.IMPORT_2);
                return false;
            }
        } else {
            m2.rollbackTo();
        }
        boolean success = Whitespace.freshLine(builder);
        m.done(ElmTypes.IMPORT_2);
        return success;
    }

    private static void docComment(PsiBuilder builder) {
        comment(builder, ElmTypes.BEGIN_DOC_COMMENT, ElmTypes.DOC_COMMENT);
    }

    private static boolean comment(PsiBuilder builder, IElementType startToken, IElementType type) {
        if (builder.getTokenType() != startToken) {
            return false; //short circuit for or in nested comments
        }
        Marker m = builder.mark();
        boolean success = Basic.sequence(builder,
                Basic.expect(startToken),
                Basic.many(Basic.or(Basic.expect(ElmTypes.COMMENT_CONTENT), MyElmParser::multilineComment)),
                Basic.expect(ElmTypes.END_COMMENT)
        );
        if (!success) {
            IElementType token = ElmTypes.END_COMMENT;
            consumeUntil(builder, token);
        }
        m.done(type);
        return success;
    }

    private static boolean multilineComment(PsiBuilder builder) {
        return comment(builder, ElmTypes.BEGIN_COMMENT, ElmTypes.MULTILINE_COMMENT);
    }


    private void moduleDeclaration(@NotNull PsiBuilder builder) {
        Marker marker = builder.mark();
        boolean success = Basic.sequence(builder,
                Basic.expect(ElmTypes.MODULE),
                Whitespace::maybeWhitespace,
                MyElmParser::dottedCapVar,
                MyElmParser.listing(MyElmParser::exportValue),
                Whitespace::maybeWhitespace,
                Basic.expect(ElmTypes.WHERE),
                Whitespace::freshLine
        );
        if (!success) {
            consumeUntil(builder, ElmTypes.NEW_LINE);
        }
        marker.done(ElmTypes.MODULE_HEADER);
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
                Basic.simpleOr(builder,
                        Basic.expect(ElmTypes.OPEN_LISTING),
                        MyElmParser.listingValues(listedValue));
    }

    private static Parser listingValues(Parser listedValue) {
        return (builder) -> listedValue.apply(builder) && Basic.simpleMany(builder, paddedComma(), listedValue);
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

    private static boolean exportValue(PsiBuilder builder) {
        return Basic.simpleOr(builder,
                Basic.expect(ElmTypes.LOW_VAR),
                MyElmParser::operator,
                MyElmParser::typeExport);
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

    private static boolean dottedCapVar(@NotNull PsiBuilder builder) {
        Marker m = builder.mark();
        boolean success = Basic.sequence(builder,
                Basic.expect(ElmTypes.CAP_VAR),
                Basic.simpleMany(ElmTypes.DOT, ElmTypes.CAP_VAR)
        );
        m.done(ElmTypes.DOTTED_CAP_VAR);
        return success;
    }

    private static void consumeUntil(PsiBuilder builder, IElementType token) {
        while (!builder.eof() && builder.getTokenType() != token) {
            builder.advanceLexer();
        }
        if (!builder.eof()) {
            builder.advanceLexer();
        }
    }

    private void consumeRest(PsiBuilder builder) {
        while (!builder.eof()) {
            builder.advanceLexer();
        }
    }
}
