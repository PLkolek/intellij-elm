package mkolaczek.elm.features.formatting;

import com.intellij.formatting.*;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SyntheticBlock implements Block {
    @Nullable
    private final Wrap wrap;
    @Nullable
    private final Alignment alignment;
    private final SpacingBuilder spacing;
    private final Indent indent;
    private final List<Block> childBlocks;

    protected SyntheticBlock(@Nullable Wrap wrap,
                             @Nullable Alignment alignment,
                             Indent indent,
                             SpacingBuilder spacing,
                             List<Block> childBlocks) {
        this.wrap = wrap;
        this.alignment = alignment;
        this.spacing = spacing;
        this.indent = indent;
        this.childBlocks = childBlocks;
    }

    @NotNull
    static SyntheticBlock chopped(SpacingBuilder spacing, Alignment alignment, Wrap chopDown, List<Block> children) {
        return new SyntheticBlock(chopDown, alignment, Indent.getNoneIndent(), spacing, children);
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
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacing.getSpacing(this, child1, child2);
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
}
