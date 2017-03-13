package mkolaczek.elm.features.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ElmAbstractBlock extends AbstractBlock {
    protected final SpacingBuilder spacing;

    protected ElmAbstractBlock(@NotNull ASTNode node,
                               @NotNull SpacingBuilder spacing,
                               @Nullable Wrap wrap) {
        super(node, wrap, null);
        //alignment must be null, otherwise wrapped blocks align to beginning of this one
        this.spacing = spacing;
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
}
