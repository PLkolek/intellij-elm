package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ElmBlock extends AbstractBlock {

    private final Set<IElementType> chopLocations;
    private final Set<IElementType> toIndent;
    private final Set<IElementType> toFlatten;
    private final SpacingBuilder spacing;

    ElmBlock(@NotNull ASTNode node,
             SpacingBuilder spacing,
             Wrap wrap,
             Set<IElementType> chopLocations, Set<IElementType> toIndent, Set<IElementType> toFlatten) {
        super(node,
                wrap,
                null); //alignment must be null, otherwise wrapped blocks align to beginning of this one
        this.spacing = spacing;
        this.chopLocations = chopLocations;
        this.toIndent = toIndent;
        this.toFlatten = toFlatten;
    }


    public static ElmBlock simple(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return simple(node, spacing, Wrap.createWrap(WrapType.NONE, false));
    }

    public static ElmBlock simple(ASTNode node, SpacingBuilder spacing, Wrap wrap) {
        return new ElmBlock(node, spacing, wrap, ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of());
    }

    public static ElmBlock complex(@NotNull ASTNode node,
                                   SpacingBuilder spacing,
                                   Set<IElementType> chopLocations,
                                   Set<IElementType> toIndent,
                                   Set<IElementType> toFlatten) {

        return new ElmBlock(node, spacing, Wrap.createWrap(WrapType.NONE, false), chopLocations, toIndent, toFlatten);
    }

    @Override
    protected List<Block> buildChildren() {
        List<ASTNode> childNodes = listOfChildren(myNode);
        List<Block> blocks = Lists.newArrayList();
        int i = eatNotWrapped(childNodes, blocks);
        List<Block> wrappedBlocks = groupWrapped(childNodes, i);
        if (!wrappedBlocks.isEmpty()) {
            blocks.add(SyntheticBlock.choppedItems(this, spacing, wrappedBlocks));
        }
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
            wrappedBlocks.add(SyntheticBlock.chopped(this,
                    spacing,
                    align,
                    wrap,
                    indent,
                    nextChildAndBlocks.getSecond()));
            i = nextChildAndBlocks.getFirst();
        }
        return wrappedBlocks;
    }

    private int eatNotWrapped(List<ASTNode> childNodes, List<Block> blocks) {
        int i = 0;

        while (i < childNodes.size() && !isSeparator(childNodes.get(i))) {
            blocks.add(ElmBlocks.createBlock(spacing, childNodes.get(i)));
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
        List<Block> children = Lists.newArrayList(ElmBlocks.createBlock(spacing, nodes.get(i)));
        i++;
        while (i < nodes.size() && !isSeparator(nodes.get(i))) {
            children.add(ElmBlocks.createBlock(spacing, nodes.get(i)));
            i++;
        }
        return Pair.pair(i, children);
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
        return myNode.getFirstChildNode() == null;
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }
}
