package mkolaczek.elm.features.formatting;

import com.google.common.collect.Maps;
import com.intellij.formatting.Block;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.features.formatting.custom.TypeConstructorArgsBlock;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.ExposingNode;
import mkolaczek.elm.psi.node.extensions.Surrounded;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

import static mkolaczek.elm.features.formatting.ChopDefinition.chopOn;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmBlocks {

    public static Block typeDecl(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ChopDefinition chopDef = chopOn(EQUALS, PIPE).flatten(PIPE_SEP).done();
        Wrap wrap = Wrap.createWrap(WrapType.ALWAYS, true);

        return new ElmChoppedBlock(node, spacing, wrap, chopDef);
    }

    public static Block effectSettings(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ChopDefinition chopDef = chopOn(WHERE, LBRACKET, COMMA, RBRACKET)
                .flatten(COMMA_SEP, EFFECT_MODULE_SETTINGS_LIST)
                .indent(LBRACKET, COMMA, RBRACKET)
                .done();
        Wrap wrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);

        return new ElmChoppedBlock(node, spacing, wrap, chopDef);
    }

    private static Block recordType(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return wrappedType(node, spacing, LBRACKET, RBRACKET);
    }

    private static Block tupleType(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return wrappedType(node, spacing, LPAREN, RPAREN);
    }

    @NotNull
    private static Block wrappedType(@NotNull ASTNode node,
                                     @NotNull SpacingBuilder spacing,
                                     Token lparen,
                                     Token rparen) {
        if (((Surrounded) node.getPsi()).isEmpty()) {
            return ElmBlock.simple(node, spacing);
        }
        ChopDefinition chopDef = ChopDefinition.surrounded(lparen, rparen);
        Wrap wrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);
        return new ElmChoppedBlock(node, spacing, wrap, chopDef);
    }


    public static Block exposing(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ExposingNode exposingNode = (ExposingNode) node.getPsi();
        if (exposingNode.valueList() == null || exposingNode.valueList().isOpenListing()) {
            return ElmBlock.simple(node, spacing, Wrap.createWrap(WrapType.NORMAL, true));
        }

        ChopDefinition chopDef = chopOn(Tokens.EXPOSING, LPAREN, COMMA, RPAREN)
                .flatten(MODULE_VALUE_LIST, COMMA_SEP)
                .indent(LPAREN, COMMA, RPAREN)
                .done();
        Wrap wrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);

        return new ElmChoppedBlock(node, spacing, wrap, chopDef);
    }

    @NotNull
    static Block createBlock(SpacingBuilder spacing, ASTNode child) {
        Map<IElementType, BiFunction<ASTNode, SpacingBuilder, Block>> map = Maps.newHashMap();
        map.put(EXPOSING_NODE, ElmBlocks::exposing);
        map.put(EFFECT_MODULE_SETTINGS, ElmBlocks::effectSettings);
        map.put(TYPE_DECL_DEF_NODE, ElmBlocks::typeDecl);
        map.put(RECORD_TYPE, ElmBlocks::recordType);
        map.put(TUPLE_TYPE, ElmBlocks::tupleType);
        map.put(TYPE_CONSTRUCTOR_ARGS, TypeConstructorArgsBlock::new);

        if (map.containsKey(child.getElementType())) {
            return map.get(child.getElementType()).apply(child, spacing);
        }

        return ElmBlock.simple(child, spacing);
    }
}
