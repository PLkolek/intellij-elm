package mkolaczek.elm.features.formatting.custom;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.features.formatting.ChopDefinition;
import mkolaczek.elm.features.formatting.ElmChoppedBlock;
import mkolaczek.elm.features.formatting.SyntheticBlock;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SurroundedBlock extends ElmChoppedBlock {
    private final boolean wrapFirstLine;
    private final Wrap noneWrap;

    public SurroundedBlock(ASTNode node,
                           SpacingBuilder spacing,
                           Wrap commonWrap,
                           boolean wrapFirstLine) {
        super(node, spacing, commonWrap, ChopDefinition.surrounded(node.getElementType()));
        this.wrapFirstLine = wrapFirstLine;
        this.noneWrap = Wrap.createWrap(WrapType.NONE, false);
    }

    @NotNull
    @Override
    protected SyntheticBlock syntheticBlock(Indent indent, Alignment align, List<Block> blocks, int index) {
        if (index == 0 && !wrapFirstLine) {
            return new SyntheticBlock(this, Wrap.createWrap(WrapType.NONE, false), align, indent, spacing, blocks);
        }
        return super.syntheticBlock(indent, align, blocks, index);
    }

    @NotNull
    @Override
    protected Block createBlock(ASTNode node, int i) {
        if (node.getElementType() == Elements.RECORD_TYPE || node.getElementType() == Elements.TUPLE_TYPE) {
            Wrap wrap = i == 0 ? Wrap.createWrap(WrapType.NONE, false) : myWrap;
            return new SurroundedBlock(node, spacing, myWrap, false);
        }
        return super.createBlock(node, i);
    }

    @Nullable
    @Override
    public Wrap getWrap() {
        return wrapFirstLine ? myWrap : noneWrap;
    }
}
