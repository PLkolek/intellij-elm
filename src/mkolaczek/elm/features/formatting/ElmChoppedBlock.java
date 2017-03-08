package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.ASTUtil;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static mkolaczek.elm.psi.Tokens.*;

public class ElmChoppedBlock extends AbstractBlock {
    private final SpacingBuilder spacingBuilder;

    private final Set<IElementType> choppedElements;
    private final IElementType valueList;

    private ElmChoppedBlock(ASTNode node,
                            SpacingBuilder spacingBuilder,
                            Wrap chopDown, Set<IElementType> choppedElements,
                            IElementType valueList) {
        super(node, chopDown, Alignment.createAlignment());
        this.spacingBuilder = spacingBuilder;
        this.choppedElements = choppedElements;
        this.valueList = valueList;
    }

    public static ElmChoppedBlock exposing(@NotNull ASTNode node,
                                           @NotNull SpacingBuilder spacingBuilder,
                                           Wrap chopDown) {
        return new ElmChoppedBlock(node,
                spacingBuilder,
                chopDown,
                ImmutableSet.of(COMMA, RPAREN, LPAREN, EXPOSING),
                Elements.MODULE_VALUE_LIST);
    }

    public static ElmChoppedBlock effectProperties(ASTNode node, SpacingBuilder spacingBuilder, Wrap chopDown) {
        return new ElmChoppedBlock(node,
                spacingBuilder,
                chopDown, ImmutableSet.of(COMMA, LBRACKET, RBRACKET, EXPOSING),
                Elements.EFFECT_MODULE_SETTINGS_LIST);
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = ASTUtil.firstSignificantChild(myNode);
        while (child != null) {
            IElementType type = child.getElementType();
            if (type == valueList) {
                ASTNode child2 = ASTUtil.firstSignificantChild(child);
                while (child2 != null) {
                    blocks.add(createBlock(child2, getWrap()));
                    child2 = ASTUtil.nextSignificant(child2);
                }
            } else {
                ElmBlock block = createBlock(child, getWrap());
                blocks.add(block);
            }
            child = ASTUtil.nextSignificant(child);
        }
        return blocks;
    }

    @NotNull
    private ElmBlock createBlock(ASTNode child, Wrap chopDownWrap) {
        IElementType type = child.getElementType();
        if (choppedElements.contains(type)) {
            return new ElmBlock(child, spacingBuilder, chopDownWrap);
        } else {
            return new ElmBlock(child, spacingBuilder);
        }
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
