package mkolaczek.elm.features.formatting;

import com.google.common.collect.Lists;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.ASTUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElmBlock extends ElmAbstractBlock {

    ElmBlock(@NotNull ASTNode node,
             SpacingBuilder spacing,
             Wrap wrap) {
        super(node, spacing, wrap
        );
    }


    public static ElmBlock simple(@NotNull ASTNode node, @NotNull SpacingBuilder spacing) {
        return simple(node, spacing, Wrap.createWrap(WrapType.NONE, false));
    }

    public static ElmBlock simple(ASTNode node, SpacingBuilder spacing, Wrap wrap) {
        return new ElmBlock(node,
                spacing,
                wrap
        );
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

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }
}
