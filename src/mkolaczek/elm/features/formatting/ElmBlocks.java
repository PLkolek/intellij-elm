package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.intellij.formatting.Block;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Token;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.elm.psi.node.ExposingNode;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;
import static mkolaczek.elm.ASTUtil.prevSignificant;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmBlocks {

    public static Block typeDecl(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        Set<IElementType> chopLocations = ImmutableSet.of(Tokens.EQUALS, Tokens.PIPE);
        Set<IElementType> flatten = ImmutableSet.of(PIPE_SEP);
        Set<IElementType> toIndent = ImmutableSet.of();
        Wrap wrap = Wrap.createWrap(WrapType.ALWAYS, true);
        return ElmChoppedBlock.complex(node, spacing, wrap, chopLocations, toIndent, flatten);
    }

    public static Block effectSettings(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        Set<IElementType> chopLocations = ImmutableSet.of(WHERE, LBRACKET, COMMA, RBRACKET);
        Set<IElementType> flatten = ImmutableSet.of(COMMA_SEP, EFFECT_MODULE_SETTINGS_LIST);
        Set<IElementType> toIndent = ImmutableSet.of(LBRACKET, COMMA, RBRACKET);
        Wrap wrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);
        return ElmChoppedBlock.complex(node, spacing, wrap, chopLocations, toIndent, flatten);
    }

    private static Block recordType(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return wrappedType(node, spacing, LBRACKET, RBRACKET, WrapType.ALWAYS);
    }

    private static Block tupleType(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return wrappedType(node, spacing, LPAREN, RPAREN, WrapType.CHOP_DOWN_IF_LONG);
    }

    private static Block typeConstructorArgs(ASTNode node, SpacingBuilder spacing) {
        Set<IElementType> chopLocations = ImmutableSet.of(TYPE_TERM);
        Set<IElementType> flatten = ImmutableSet.of();
        Set<IElementType> toIndent = ImmutableSet.of();
        Wrap wrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);
        return ElmChoppedBlock.complex(node, spacing, wrap, chopLocations, toIndent, flatten);
    }

    @NotNull
    private static Block wrappedType(@NotNull ASTNode node,
                                     @NotNull SpacingBuilder spacing,
                                     Token lparen,
                                     Token rparen,
                                     WrapType chopDownIfLong) {
        Set<IElementType> chopLocations = ImmutableSet.of(lparen, COMMA, rparen);
        Set<IElementType> flatten = ImmutableSet.of(COMMA_SEP);
        Set<IElementType> toIndent = ImmutableSet.of();
        ASTNode parent = node.getTreeParent();
        assert parent.getElementType() == Elements.TYPE_TERM;
        IElementType prevType = ofNullable(prevSignificant(parent)).map(ASTNode::getElementType).orElse(null);
        Wrap wrap = Wrap.createWrap(chopDownIfLong, prevType != Tokens.COMMA);
        return ElmChoppedBlock.complex(node, spacing, wrap, chopLocations, toIndent, flatten);
    }


    public static Block exposing(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ExposingNode exposingNode = (ExposingNode) node.getPsi();
        if (exposingNode.valueList() == null || exposingNode.valueList().isOpenListing()) {
            return ElmBlock.simple(node, spacing, Wrap.createWrap(WrapType.NORMAL, true));
        }

        Set<IElementType> chopLocations = ImmutableSet.of(Tokens.EXPOSING, LPAREN, COMMA, RPAREN);
        Set<IElementType> flatten = ImmutableSet.of(MODULE_VALUE_LIST, COMMA_SEP);
        Set<IElementType> toIndent = ImmutableSet.of(LPAREN, COMMA, RPAREN);
        Wrap wrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);
        return ElmChoppedBlock.complex(node, spacing, wrap, chopLocations, toIndent, flatten);
    }

    @NotNull
    static Block createBlock(SpacingBuilder spacing, ASTNode child) {
        Map<IElementType, BiFunction<ASTNode, SpacingBuilder, Block>> map = Maps.newHashMap();
        map.put(EXPOSING_NODE, ElmBlocks::exposing);
        map.put(EFFECT_MODULE_SETTINGS, ElmBlocks::effectSettings);
        map.put(TYPE_DECL_DEF_NODE, ElmBlocks::typeDecl);
        map.put(RECORD_TYPE, ElmBlocks::recordType);
        map.put(TUPLE_TYPE, ElmBlocks::tupleType);
        map.put(TYPE_CONSTRUCTOR_ARGS, ElmBlocks::typeConstructorArgs);

        if (map.containsKey(child.getElementType())) {
            return map.get(child.getElementType()).apply(child, spacing);
        }

        return ElmBlock.simple(child, spacing);
    }
}
