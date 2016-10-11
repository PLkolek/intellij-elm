package mkolaczek.elm.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ElmBlock extends AbstractBlock {
    private final Indent indent;
    private SpacingBuilder spacingBuilder;

    protected ElmBlock(@NotNull ASTNode node, @Nullable Wrap wrap, Indent indent, @Nullable Alignment alignment,
                       SpacingBuilder spacingBuilder) {
        super(node, wrap, alignment);
        this.indent = indent;
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            IElementType type = child.getElementType();
            if (type != TokenType.WHITE_SPACE && type != TokenType.ERROR_ELEMENT) {
                if (type == ElmElementTypes.MODULE_VALUE_LIST) {
                    blocks.add(new ElmListingBlock(child, spacingBuilder));
                } else {
                    blocks.add(new ElmBlock(child, ElmWrapFactory.createWrap(type), ElmIndentFactory.createIndent(type), Alignment.createAlignment(),
                            spacingBuilder));
                }
            }
            child = child.getTreeNext();
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
