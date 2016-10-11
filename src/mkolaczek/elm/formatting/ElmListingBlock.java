package mkolaczek.elm.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static mkolaczek.elm.psi.ElmTokenTypes.*;

public class ElmListingBlock extends AbstractBlock {
    private SpacingBuilder spacingBuilder;

    protected ElmListingBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder) {
        super(node, Wrap.createWrap(WrapType.NORMAL, false), Alignment.createAlignment());
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        Wrap chopDownWrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true);
        //chopDownWrap.ignoreParentWraps();
        while (child != null) {
            IElementType type = child.getElementType();
            if (type != TokenType.WHITE_SPACE && type != TokenType.ERROR_ELEMENT) {
                if (type == COMMA || type == RPAREN) {
                    blocks.add(new ElmBlock(child, chopDownWrap, ElmIndentFactory.createIndent(type), Alignment.createAlignment(),
                            spacingBuilder));
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
        return Indent.getNormalIndent();
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        checkArgument(child1 instanceof AbstractBlock, "child1 must wrap AST node");
        checkArgument(child2 instanceof AbstractBlock, "child2 must wrap AST node");
        AbstractBlock c1 = (AbstractBlock) child1;
        if (c1.getNode().getElementType() == LPAREN) {
            TextRange textRange = getNode().getPsi().getTextRange();
            return new ElmAfterLParenDependantSpacing(textRange);
        }
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
