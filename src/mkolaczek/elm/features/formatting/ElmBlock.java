package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ElmBlock extends AbstractBlock {
    private final SpacingBuilder spacing;

    ElmBlock(@NotNull ASTNode node,
             SpacingBuilder spacing,
             Wrap wrap,
             Set<IElementType> toFlatten) {
        super(node,
                wrap,
                null); //alignment must be null, otherwise wrapped blocks align to beginning of this one
        this.spacing = spacing;
    }


    public static ElmBlock simple(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return simple(node, spacing, Wrap.createWrap(WrapType.NONE, false));
    }

    public static ElmBlock simple(ASTNode node, SpacingBuilder spacing, Wrap wrap) {
        return new ElmBlock(node,
                spacing,
                wrap,
                ImmutableSet.of());
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = Lists.newArrayList();
        ASTNode child = ASTUtil.firstSignificantChild(myNode);
        while (child != null) {
            blocks.add(ElmBlocks.createBlock(spacing, child));
            child = ASTUtil.nextSignificant(child);
        }
        return blocks;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        child2 = SyntheticBlock.leftAstBlock(child2);
        child1 = SyntheticBlock.rightAstBlock(child1);
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
