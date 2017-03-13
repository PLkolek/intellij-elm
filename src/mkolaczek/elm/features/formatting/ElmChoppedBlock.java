package mkolaczek.elm.features.formatting;

import com.google.common.collect.Lists;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.ASTUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElmChoppedBlock extends ElmAbstractBlock {

    private final Wrap childrenWrap;
    private final ChopDefinition chopDefinition;

    ElmChoppedBlock(@NotNull ASTNode node,
                    SpacingBuilder spacing,
                    Wrap childrenWrap,
                    ChopDefinition chopDefinition) {
        super(node,
                spacing,
                //consider                 |
                //import A exposing (A, B, C)
                //                         |
                //when intellij finds out, that the line exceeds the margin and has be wrapped (on the right parenthesis)
                //it goes back to to the "wrap candidate" ???, which is the exposing node. There, a stack of all wraps from the
                //block is gathered, from the most general to most specific, and it takes the FIRST WRAP and wraps the line
                //even if it is NO_WRAP!!! To force chopping the line, the most general wrap must be the same chop down wrap
                //as for all the child elements. Sorry
                childrenWrap);
        this.childrenWrap = childrenWrap;
        this.chopDefinition = chopDefinition;
    }


    @Override
    protected List<Block> buildChildren() {
        return groupWrapped(listOfChildren(myNode));
    }

    @NotNull
    private List<Block> groupWrapped(List<ASTNode> childNodes) {
        List<Block> wrappedBlocks = Lists.newArrayList();
        Alignment alignment = Alignment.createAlignment();
        Alignment indentedAlignment = Alignment.createAlignment();
        int i = 0;
        while (i < childNodes.size()) {
            boolean isIndented = chopDefinition.shouldIndent(childNodes.get(i));
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
            if (chopDefinition.shouldFlatten(child)) {
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
        while (i < nodes.size() && !chopDefinition.shouldWrap(nodes.get(i))) {
            children.add(ElmBlocks.createBlock(spacing, nodes.get(i)));
            i++;
        }
        return children;
    }

    @Override
    public Indent getIndent() {
        return Indent.getNormalIndent();
    }
}
