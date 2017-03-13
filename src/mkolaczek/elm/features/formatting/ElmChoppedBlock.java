package mkolaczek.elm.features.formatting;

import com.google.common.collect.Lists;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ElmChoppedBlock extends ElmAbstractBlock {

    private final Wrap childrenWrap;
    private final Set<IElementType> chopLocations;
    private final Set<IElementType> toIndent;
    private final Set<IElementType> toFlatten;

    ElmChoppedBlock(@NotNull ASTNode node,
                    SpacingBuilder spacing,
                    Wrap wrap,
                    Wrap childrenWrap,
                    Set<IElementType> chopLocations, Set<IElementType> toIndent, Set<IElementType> toFlatten) {
        super(node, spacing, wrap);
        this.childrenWrap = childrenWrap;
        this.chopLocations = chopLocations;
        this.toIndent = toIndent;
        this.toFlatten = toFlatten;
    }


    public static ElmChoppedBlock complex(@NotNull ASTNode node,
                                          SpacingBuilder spacing,
                                          Wrap childrenWrap,
                                          Set<IElementType> chopLocations,
                                          Set<IElementType> toIndent,
                                          Set<IElementType> toFlatten) {

        return new ElmChoppedBlock(node,
                spacing,
                childrenWrap,
                //consider                 |
                //import A exposing (A, B, C)
                //                         |
                //when intellij finds out, that the line exceeds the margin and has be wrapped (on the right parenthesis)
                //it goes back to to the "wrap candidate" ???, which is the exposing node. There, a stack of all wraps from the
                //block is gathered, from the most general to most specific, and it takes the FIRST WRAP and wraps the line
                //even if it is NO_WRAP!!! To force chopping the line, the most general wrap must be the same chop down wrap
                //as for all the child elements. Sorry
                childrenWrap,
                chopLocations,
                toIndent,
                toFlatten);
    }

    @Override
    protected List<Block> buildChildren() {
        List<ASTNode> childNodes = listOfChildren(myNode);
        List<Block> blocks = Lists.newArrayList();
        List<Block> wrappedBlocks = groupWrapped(childNodes);
        if (!wrappedBlocks.isEmpty()) {
            blocks.add(SyntheticBlock.choppedItems(this, spacing, wrappedBlocks));
        }
        return blocks;
    }

    @NotNull
    private List<Block> groupWrapped(List<ASTNode> childNodes) {
        List<Block> wrappedBlocks = Lists.newArrayList();
        Alignment alignment = Alignment.createAlignment();
        Alignment indentedAlignment = Alignment.createAlignment();
        int i = 0;
        while (i < childNodes.size()) {
            boolean isIndented = toIndent.contains(childNodes.get(i).getElementType());
            Indent indent = isIndented ? Indent.getNormalIndent() : Indent.getNoneIndent();
            Alignment align = isIndented ? indentedAlignment : alignment;
            List<Block> blocks = scanSubBlock(childNodes, i);
            wrappedBlocks.add(SyntheticBlock.chopped(this,
                    spacing,
                    align,
                    childrenWrap,
                    indent,
                    blocks));
            i += blocks.size();
        }
        return wrappedBlocks;
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


    private List<Block> scanSubBlock(List<ASTNode> nodes, int i) {
        List<Block> children = Lists.newArrayList(ElmBlocks.createBlock(spacing, nodes.get(i)));
        i++;
        while (i < nodes.size() && !isSeparator(nodes.get(i))) {
            children.add(ElmBlocks.createBlock(spacing, nodes.get(i)));
            i++;
        }
        return children;
    }

    private boolean isSeparator(ASTNode child) {
        return chopLocations.contains(child.getElementType());
    }
}
