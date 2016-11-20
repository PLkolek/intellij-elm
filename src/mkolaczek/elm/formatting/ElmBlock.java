package mkolaczek.elm.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import mkolaczek.elm.psi.ElmElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ElmBlock extends AbstractBlock {
    private final Indent indent;
    private SpacingBuilder spacingBuilder;

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
        while (child != null) {
            IElementType type = child.getElementType();
            if (type == ElmElementTypes.EXPOSING_NODE) {
                blocks.add(ElmChoppedBlock.exposing(child, spacingBuilder));
            } else if (type == ElmElementTypes.EFFECT_MODULE_SETTINGS) {
                blocks.add(ElmChoppedBlock.effectProperties(child, spacingBuilder));
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
