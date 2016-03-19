package mkolaczek.elm;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmTypes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

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
        boolean success = sequence(builder,
                expect(ElmTypes.MODULE),
                Whitespace::maybeWhitespace,
                this::dottedCapVar,
                MyElmParser::exports,
                Whitespace::maybeWhitespace,
                expect(ElmTypes.WHERE),
                Whitespace::freshLine
        );
        if (!success) {
            consumeLine(builder);
        }
        marker.done(ElmTypes.MODULE_HEADER);
        return success;
    }


    private static boolean sequence(PsiBuilder builder, Function<PsiBuilder, Boolean>... parsers) {
        for (Function<PsiBuilder, Boolean> parser : parsers) {
            if (!parser.apply(builder)) {
                return false;
            }
        }
        return true;
    }

    private static boolean exports(PsiBuilder builder) {
        Marker m = builder.mark();
        Whitespace.maybeWhitespace(builder);
        if (builder.getTokenType() != ElmTypes.LPAREN) {
            m.rollbackTo();
            return true;
        }
        boolean success = sequence(builder,
                expect(ElmTypes.LPAREN),
                Whitespace::maybeWhitespace,
                expect(ElmTypes.RPAREN)
        );
        m.done(ElmTypes.MODULE_VALUE_LIST);
        return success;
    }

    private boolean dottedCapVar(@NotNull PsiBuilder builder) {
        Marker m = builder.mark();
        boolean success = sequence(builder, expect(ElmTypes.CAP_VAR), many(ElmTypes.DOT, ElmTypes.CAP_VAR));
        m.done(ElmTypes.DOTTED_CAP_VAR);
        return success;
    }

    private static Function<PsiBuilder, Boolean> many(IElementType... tokens) {
        checkArgument(tokens != null && tokens.length >= 1);
        return (PsiBuilder builder) -> {
            while (builder.getTokenType() == tokens[0]) {
                if (!simpleExpect(builder, tokens)) {
                    return false;
                }
            }
            return true;
        };
    }

    private static Function<PsiBuilder, Boolean> expect(IElementType... tokens) {
        return (PsiBuilder builder) -> simpleExpect(builder, tokens);
    }

    @NotNull
    private static Boolean simpleExpect(PsiBuilder builder, IElementType... tokens) {
        for (IElementType token : tokens) {
            if (builder.getTokenType() != token) {
                builder.error(token.toString() + " expected");
                return false;
            }
            builder.advanceLexer();
        }
        return true;
    }

    private void consumeLine(PsiBuilder builder) {
        while (!builder.eof() && builder.getTokenType() != ElmTypes.NEW_LINE) {
            builder.advanceLexer();
        }
    }
}
