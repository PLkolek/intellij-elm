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

    private final Set<IElementType> separators = ImmutableSet.of(Tokens.EQUALS, Tokens.PIPE);
    private final Set<IElementType> toFlatten = ImmutableSet.of(Elements.PIPE_SEP);
    private final SpacingBuilder spacing;

    TypeDeclBlock(@NotNull ASTNode node, SpacingBuilder spacing) {
        super(node,
                Wrap.createWrap(WrapType.NONE, false),
                null); //alignment must be null, otherwise wrapped blocks align to beginning of this one
        this.spacing = spacing;
    }

    @Override
    protected List<Block> buildChildren() {
        List<ASTNode> childNodes = listOfChildren(myNode);
        List<Block> blocks = Lists.newArrayList();
        Wrap chopDown = Wrap.createWrap(WrapType.ALWAYS, true);
        int i = 0;

        while (i < childNodes.size() && !isSeparator(childNodes.get(i))) {
            blocks.add(simpleBlock(childNodes.get(i)));
            i++;
        }
        List<Block> wrappedBlocks = Lists.newArrayList();
        Alignment alignment = Alignment.createAlignment();
        while (i < childNodes.size()) {
            Pair<Integer, List<Block>> nextChildAndBlocks = scanSubBlock(childNodes, i);
            wrappedBlocks.add(SyntheticBlock.chopped(spacing, alignment, chopDown, nextChildAndBlocks.getSecond()));
            i = nextChildAndBlocks.getFirst();
        }
        blocks.add(new SyntheticBlock(Wrap.createWrap(WrapType.ALWAYS, true), Alignment.createAlignment(),
                Indent.getNormalIndent()
                , spacing, wrappedBlocks));
        return blocks;
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
        return separators.contains(child.getElementType());
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
