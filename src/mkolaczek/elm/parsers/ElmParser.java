package mkolaczek.elm.parsers;

import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.parsers.faultTolerant.FTModule;
import mkolaczek.elm.parsers.faultTolerant.FTParser;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.parsers.Basic.dottedCapVar;
import static mkolaczek.elm.parsers.Basic.paddedEquals;
import static mkolaczek.elm.parsers.Combinators.*;

public class ElmParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        Marker rootMarker = builder.mark();
        PsiBuilder.Marker module = builder.mark();
        int startingOffset = builder.getCurrentOffset();
        program(builder);
        if (!builder.eof()) {
            builder.error("EOF expected");
        }
        consumeRest(builder);
        if (startingOffset != builder.getCurrentOffset()) {
            module.done(Elements.MODULE_NODE);
        } else {
            module.drop();
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void program(@NotNull PsiBuilder builder) {
        FTParser parser = FTModule.moduleHeader();
        parser.computeNextTokens(Sets.newHashSet());
        parser.parse(builder);
        declarations(builder);
    }

    private static void declarations(@NotNull PsiBuilder builder) {
        declaration(builder);
        Combinators.simpleMany(builder, ElmParser::freshDef);
    }

    private static boolean declaration(@NotNull PsiBuilder builder) {
        return Combinators.simpleOr(builder, Comment.docComment(), ElmParser.typeDecl());
    }

    private static boolean freshDef(PsiBuilder builder) {
        Marker marker = builder.mark();
        Whitespace.freshLine(builder);
        if (!builder.eof()) {
            char currentChar = builder.getOriginalText().charAt(builder.getCurrentOffset());
            if (Character.isLetter(currentChar) || currentChar == '_') {
                marker.drop();
                return declaration(builder);
            }
        }
        marker.rollbackTo();
        return false;

    }

    private static NamedParser typeDecl() {
        return NamedParser.of("Type declaration", ElmParser::typeDecl);
    }


    public static boolean typeDecl(@NotNull PsiBuilder builder) {
        if (builder.getTokenType() != Tokens.TYPE) {
            return false;
        }
        Marker marker = builder.mark();
        boolean result = simpleSequence(builder, expect(Tokens.TYPE), Whitespace::forcedWS);
        boolean isAlias = false;
        if (result && builder.getTokenType() == Tokens.ALIAS) {
            result = simpleSequence(builder, expect(Tokens.ALIAS), Whitespace::forcedWS);
            isAlias = true;
        }
        result = result && simpleSequence(builder,
                expect(Tokens.CAP_VAR),
                spacePrefix(expect(Tokens.LOW_VAR)),
                paddedEquals()
        );
        if (isAlias) {
            result = result && typeExpr(builder);
        }

        if (!result) {
            OnError.consumeUntil(builder, Tokens.NEW_LINE);
        }
        marker.done(Elements.TYPE_DECLARATION);
        return true;
    }

    private static boolean typeExpr(PsiBuilder builder) {
        return simpleOr(builder, typeApp());
    }

    private static NamedParser typeApp() {
        Parser p = sequence(
                or(dottedCapVar(), tryP(tupleCtor()))
                //spacePrefix(term())
        );
        return NamedParser.of("App", p);
    }

    /*private static NamedParser term() {
        return NamedParser.of("Type term", or(tuple()));
    }

    private static NamedParser tuple() {
        return NamedParser.of("Type tuple", Combinators.);
    }*/

    private static NamedParser tupleCtor() {
        Parser parser = sequence(expect(Tokens.LPAREN),
                Basic.padded(Tokens.COMMA_OP),
                expect(Tokens.RPAREN));
        return NamedParser.of("TupleCtor", parser);
    }


    private void consumeRest(PsiBuilder builder) {
        while (!builder.eof()) {
            builder.advanceLexer();
        }
    }
}
