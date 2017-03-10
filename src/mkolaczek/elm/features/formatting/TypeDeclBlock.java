package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.Tokens;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TypeDeclBlock extends AbstractBlock {

    private final Set<IElementType> chopLocations;
    private final Set<IElementType> toIndent;
    private final Set<IElementType> toFlatten;
    private final SpacingBuilder spacing;

    TypeDeclBlock(@NotNull ASTNode node,
                  SpacingBuilder spacing,
                  Set<IElementType> chopLocations, Set<IElementType> toIndent, Set<IElementType> toFlatten) {
        super(node,
                Wrap.createWrap(WrapType.NONE, false),
                null); //alignment must be null, otherwise wrapped blocks align to beginning of this one
        this.spacing = spacing;
        this.chopLocations = chopLocations;
        this.toIndent = toIndent;
        this.toFlatten = toFlatten;
    }

    public static TypeDeclBlock typeDecl(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ImmutableSet<IElementType> chopLocations = ImmutableSet.of(Tokens.EQUALS, Tokens.PIPE);
        ImmutableSet<IElementType> flatten = ImmutableSet.of(Elements.PIPE_SEP);
        Set<IElementType> toIndent = ImmutableSet.of();
        return new TypeDeclBlock(node, spacing, chopLocations, toIndent, flatten);
    }

    public static TypeDeclBlock effectSettings(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ImmutableSet<IElementType> chopLocations = ImmutableSet.of(Tokens.WHERE,
                Tokens.LBRACKET,
                Tokens.COMMA,
                Tokens.RBRACKET);
        ImmutableSet<IElementType> flatten = ImmutableSet.of(Elements.COMMA_SEP, Elements.EFFECT_MODULE_SETTINGS_LIST);
        Set<IElementType> toIndent = ImmutableSet.of(Tokens.LBRACKET, Tokens.COMMA, Tokens.RBRACKET);
        return new TypeDeclBlock(node, spacing, chopLocations, toIndent, flatten);
    }

    public static Block exposing(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        ImmutableSet<IElementType> chopLocations = ImmutableSet.of(Tokens.EXPOSING,
                Tokens.LPAREN,
                Tokens.COMMA,
                Tokens.RPAREN);
        ImmutableSet<IElementType> flatten = ImmutableSet.of(Elements.MODULE_VALUE_LIST, Elements.COMMA_SEP);
        Set<IElementType> toIndent = ImmutableSet.of(Tokens.LPAREN, Tokens.COMMA, Tokens.RPAREN);
        return new TypeDeclBlock(node, spacing, chopLocations, toIndent, flatten);
    }

    @Override
    protected List<Block> buildChildren() {
        List<ASTNode> childNodes = listOfChildren(myNode);
        List<Block> blocks = Lists.newArrayList();
        int i = eatNotWrapped(childNodes, blocks);
        List<Block> wrappedBlocks = groupWrapped(childNodes, i);
        blocks.add(SyntheticBlock.choppedItems(spacing, wrappedBlocks));
        return blocks;
    }

    @NotNull
    private List<Block> groupWrapped(List<ASTNode> childNodes, int i) {
        List<Block> wrappedBlocks = Lists.newArrayList();
        Alignment alignment = Alignment.createAlignment();
        Alignment indentedAlignment = Alignment.createAlignment();
        Wrap wrap = Wrap.createWrap(WrapType.ALWAYS, true);
        while (i < childNodes.size()) {
            boolean isIndented = toIndent.contains(childNodes.get(i).getElementType());
            Indent indent = isIndented ? Indent.getNormalIndent() : Indent.getNoneIndent();
            Alignment align = isIndented ? indentedAlignment : alignment;
            Pair<Integer, List<Block>> nextChildAndBlocks = scanSubBlock(childNodes, i);
            wrappedBlocks.add(SyntheticBlock.chopped(spacing, align, wrap, indent, nextChildAndBlocks.getSecond()));
            i = nextChildAndBlocks.getFirst();
        }
        return wrappedBlocks;
    }

    private int eatNotWrapped(List<ASTNode> childNodes, List<Block> blocks) {
        int i = 0;

        while (i < childNodes.size() && !isSeparator(childNodes.get(i))) {
            blocks.add(simpleBlock(childNodes.get(i)));
            i++;
        }
        return i;
    }

    private List<ASTNode> listOfChildren(ASTNode node) {
        List<ASTNode> result = Lists.newArrayList();
        ASTNode child = ASTUtil.firstSignificantChild(node);
        while (child != null) {
            if (toFlatten.contains(child.getElementType())) {
                result.addAll(listOfChildren(child));
            } else {
                result.add(child);
            }
            child = ASTUtil.nextSignificant(child);
        }
        return result;
    }

    private Pair<Integer, List<Block>> scanSubBlock(List<ASTNode> nodes, int i) {
        List<Block> children = Lists.newArrayList(simpleBlock(nodes.get(i)));
        i++;
        while (i < nodes.size() && !isSeparator(nodes.get(i))) {
            children.add(simpleBlock(nodes.get(i)));
            i++;
        }
        return Pair.pair(i, children);
    }


    @NotNull
    private ElmBlock simpleBlock(ASTNode child) {
        return new ElmBlock(child, spacing);
    }

    private boolean isSeparator(ASTNode child) {
        return chopLocations.contains(child.getElementType());
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacing.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }
}
