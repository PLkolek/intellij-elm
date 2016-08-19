package mkolaczek.elm.parsers;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmTokenTypes;
import mkolaczek.elm.psi.ElmElementTypes;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.dottedCapVar;
import static mkolaczek.elm.parsers.Basic.paddedEquals;
import static mkolaczek.elm.parsers.Combinators.*;

public class MyElmParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        Marker marker = builder.mark();
        program(builder);
        if (!builder.eof()) {
            builder.error("EOF expected");
        }
        consumeRest(builder);
        marker.done(root);
        return builder.getTreeBuilt();
    }

    private void program(@NotNull PsiBuilder builder) {
        Module.module(builder);
        //    declarations(builder);
    }

    private void declarations(@NotNull PsiBuilder builder) {
        declaration(builder);
    }

    private void declaration(@NotNull PsiBuilder builder) {
        Combinators.simpleOr(builder, Comment.docComment(), MyElmParser.typeDecl());
    }

    private static NamedParser typeDecl() {
        return NamedParser.of("Type declaration", MyElmParser::typeDecl);
    }


    public static boolean typeDecl(@NotNull PsiBuilder builder) {
        if (builder.getTokenType() != ElmTokenTypes.TYPE) {
            return false;
        }
        Marker marker = builder.mark();
        boolean result = simpleSequence(builder, expect(ElmTokenTypes.TYPE), Whitespace::forcedWS);
        boolean isAlias = false;
        if (result && builder.getTokenType() == ElmTokenTypes.ALIAS) {
            result = simpleSequence(builder, expect(ElmTokenTypes.ALIAS), Whitespace::forcedWS);
            isAlias = true;
        }
        result = result && simpleSequence(builder,
                expect(ElmTokenTypes.CAP_VAR),
                spacePrefix(expect(ElmTokenTypes.LOW_VAR)),
                paddedEquals()
        );
        if (isAlias) {
            result = result && typeExpr(builder);
        }

        if (!result) {
            OnError.consumeUntil(builder, ElmTokenTypes.NEW_LINE);
        }
        marker.done(ElmElementTypes.TYPE_DECLARATION);
        return true;
    }

    private static boolean typeExpr(PsiBuilder builder) {
        return simpleOr(builder, typeApp());
    }

    private static NamedParser typeApp() {
        Parser p = sequence(
                or(dottedCapVar(), tryP(tupleCtor())),
                spacePrefix(term())
        );
        return NamedParser.of("App", p);
    }

    private static NamedParser term() {
        return NamedParser.of("Type term", or(tuple()));
    }

    private static NamedParser tuple() {
        return NamedParser.of("Type tuple", or(tuple()));
    }

    private static NamedParser tupleCtor() {
        Parser parser = sequence(expect(ElmTokenTypes.LPAREN), Basic.padded(ElmTokenTypes.COMMA_OP), expect(ElmTokenTypes.RPAREN));
        return NamedParser.of("TupleCtor", parser);
    }


    private void consumeRest(PsiBuilder builder) {
        while (!builder.eof()) {
            builder.advanceLexer();
        }
    }
}