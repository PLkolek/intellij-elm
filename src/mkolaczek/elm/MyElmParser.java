package mkolaczek.elm;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.parsers.Basic;
import mkolaczek.elm.parsers.Whitespace;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;

public class MyElmParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        Marker marker = builder.mark();
        module(builder);
        marker.done(root);
        return builder.getTreeBuilt();
    }

    private boolean module(@NotNull PsiBuilder builder) {
        Marker marker = builder.mark();
        boolean success = Basic.sequence(builder,
                Basic.expect(ElmTypes.MODULE),
                Whitespace::maybeWhitespace,
                this::dottedCapVar,
                MyElmParser::exports,
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


    private static boolean exports(PsiBuilder builder) {
        Marker m = builder.mark();
        Whitespace.maybeWhitespace(builder);
        if (builder.getTokenType() != ElmTypes.LPAREN) {
            m.rollbackTo();
            return true;
        }
        boolean success = Basic.sequence(builder,
                Basic.expect(ElmTypes.LPAREN),
                Whitespace::maybeWhitespace,
                MyElmParser::exportListing,
                Whitespace::maybeWhitespace,
                Basic.expect(ElmTypes.RPAREN)
        );
        m.done(ElmTypes.MODULE_VALUE_LIST);
        return success;
    }

    private static boolean exportListing(PsiBuilder builder) {
        return Basic.or(builder,
                Basic.expect(ElmTypes.OPEN_LISTING),
                MyElmParser::exportValues);
    }

    private static boolean exportValues(PsiBuilder builder) {
        return exportValue(builder);
    }

    private static boolean exportValue(PsiBuilder builder) {
        return Basic.or(builder,
                Basic.expect(ElmTypes.LOW_VAR),
                MyElmParser::operator
        );
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
                Basic.many(ElmTypes.DOT, ElmTypes.CAP_VAR)
        );
        m.done(ElmTypes.DOTTED_CAP_VAR);
        return success;
    }

    private void consumeLine(PsiBuilder builder) {
        while (!builder.eof() && builder.getTokenType() != ElmTypes.NEW_LINE) {
            builder.advanceLexer();
        }
    }
}
