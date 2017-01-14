package mkolaczek.elm.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.node.ElmExposingNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ElmBlock extends AbstractBlock {
    private final Indent indent;
    private final SpacingBuilder spacingBuilder;

    ElmBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder, Wrap wrap) {
        super(node, wrap, Alignment.createAlignment());
        this.indent = ElmIndentFactory.createIndent(node.getElementType());
        this.spacingBuilder = spacingBuilder;
    }

    ElmBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder) {
        this(node, spacingBuilder, ElmWrapFactory.createWrap(node.getElementType()));
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = ASTUtil.firstSignificantChild(myNode);
        Wrap chopDown = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);
        while (child != null) {
            IElementType type = child.getElementType();
            if (type == Elements.EXPOSING_NODE) {
                ElmExposingNode exposingNode = (ElmExposingNode) child.getPsi();
                if (exposingNode.valueList().isOpenListing()) {
                    blocks.add(new ElmBlock(child, spacingBuilder, chopDown));
                } else {
                    blocks.add(ElmChoppedBlock.exposing(child, spacingBuilder, chopDown));
                }
            } else if (type == Elements.EFFECT_MODULE_SETTINGS) {
                blocks.add(ElmChoppedBlock.effectProperties(child, spacingBuilder, chopDown));
            } else {
                blocks.add(new ElmBlock(child, spacingBuilder));
            }
            child = ASTUtil.nextSignificant(child);
        }
        return blocks;
    }

    @Override
    public Indent getIndent() {
        return indent;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }

}
