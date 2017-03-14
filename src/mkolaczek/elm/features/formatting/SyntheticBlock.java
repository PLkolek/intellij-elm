package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableList;
import com.intellij.formatting.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static mkolaczek.elm.psi.Tokens.LBRACKET;
import static mkolaczek.elm.psi.Tokens.LPAREN;

public class SyntheticBlock implements Block {
    private final ASTBlock parent;
    @Nullable
    private final Wrap wrap;
    @Nullable
    private final Alignment alignment;
    private final SpacingBuilder spacing;
    private final Indent indent;
    private final List<Block> childBlocks;

    public SyntheticBlock(ASTBlock parent,
                          @Nullable Wrap wrap,
                          @Nullable Alignment alignment,
                          Indent indent,
                          SpacingBuilder spacing,
                          List<Block> childBlocks) {
        this.parent = parent; //required for spacing builder to work :(
        this.wrap = wrap;
        this.alignment = alignment;
        this.spacing = spacing;
        this.indent = indent;
        this.childBlocks = ImmutableList.copyOf(childBlocks);
    }

    static Block leftAstBlock(@NotNull Block child2) {
        if (child2 instanceof SyntheticBlock) {
            child2 = ((SyntheticBlock) child2).leftAstBlock();
        }
        return child2;
    }

    static Block rightAstBlock(@Nullable Block child1) {
        if (child1 instanceof SyntheticBlock) {
            child1 = ((SyntheticBlock) child1).rightAstBlock();
        }
        return child1;
    }

    @NotNull
    @Override
    public TextRange getTextRange() {
        int start = childBlocks.get(0).getTextRange().getStartOffset();
        int end = childBlocks.get(childBlocks.size() - 1).getTextRange().getEndOffset();
        return new TextRange(start, end);
    }

    @NotNull
    @Override
    public List<Block> getSubBlocks() {
        return childBlocks;
    }

    @Nullable
    @Override
    public Wrap getWrap() {
        return wrap;
    }

    @Nullable
    @Override
    public Indent getIndent() {
        return indent;
    }

    @Nullable
    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block c1, @NotNull Block c2) {
        c2 = SyntheticBlock.leftAstBlock(c2);
        c1 = SyntheticBlock.rightAstBlock(c1);
        IElementType type = c1 instanceof AbstractBlock ? ((AbstractBlock) c1).getNode().getElementType() : null;
        if (type == LPAREN || type == LBRACKET) {
            TextRange textRange = parent.getTextRange();
            return new ElmAfterLParenDependantSpacing(textRange);
        }
        return spacing.getSpacing(parent, c1, c2);
    }

    @NotNull
    @Override
    public ChildAttributes getChildAttributes(int newChildIndex) {
        List<Block> subBlocks = getSubBlocks();
        int prevBlockIndex = newChildIndex - 1;
        if (prevBlockIndex >= 0 && prevBlockIndex < subBlocks.size()) {
            Block prevBlock = subBlocks.get(newChildIndex - 1);
            return new ChildAttributes(prevBlock.getIndent(), prevBlock.getAlignment());
        }
        return new ChildAttributes(getIndent(), null);
    }

    @Override
    public boolean isIncomplete() {
        return getSubBlocks().get(getSubBlocks().size() - 1).isIncomplete();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public Block leftAstBlock() {
        Block first = childBlocks.get(0);
        if (first instanceof SyntheticBlock) {
            return ((SyntheticBlock) first).leftAstBlock();
        }
        return first;
    }

    public Block rightAstBlock() {
        Block last = childBlocks.get(childBlocks.size() - 1);
        if (last instanceof SyntheticBlock) {
            return ((SyntheticBlock) last).rightAstBlock();
        }
        return last;
    }
}
