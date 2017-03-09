package mkolaczek.elm.features.formatting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
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
    private final Set<IElementType> flattenedElements;

    private ElmChoppedBlock(ASTNode node,
                            SpacingBuilder spacingBuilder,
                            Wrap chopDown, Set<IElementType> choppedElements,
                            Set<IElementType> flattenedElements) {
        super(node, chopDown, Alignment.createAlignment());
        this.spacingBuilder = spacingBuilder;
        this.choppedElements = choppedElements;
        this.flattenedElements = flattenedElements;
    }

    public static ElmChoppedBlock exposing(@NotNull ASTNode node,
                                           @NotNull SpacingBuilder spacingBuilder,
                                           Wrap chopDown) {
        return new ElmChoppedBlock(node,
                spacingBuilder,
                chopDown,
                ImmutableSet.of(COMMA, RPAREN, LPAREN, EXPOSING),
                Sets.newHashSet(Elements.MODULE_VALUE_LIST, Elements.COMMA_SEP));
    }

    public static ElmChoppedBlock effectProperties(ASTNode node, SpacingBuilder spacingBuilder, Wrap chopDown) {
        return new ElmChoppedBlock(node,
                spacingBuilder,
                chopDown, ImmutableSet.of(COMMA, LBRACKET, RBRACKET, EXPOSING),
                Sets.newHashSet(Elements.EFFECT_MODULE_SETTINGS_LIST, Elements.COMMA_SEP));
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        buildList(blocks, myNode);
        return blocks;
    }

    private void buildList(List<Block> blocks, ASTNode listRoot) {
        ASTNode child = ASTUtil.firstSignificantChild(listRoot);
        while (child != null) {
            IElementType type = child.getElementType();
            if (flattenedElements.contains(type)) {
                buildList(blocks, child);
            } else {
                ElmBlock block = createBlock(child, getWrap());
                blocks.add(block);
            }
            child = ASTUtil.nextSignificant(child);
        }
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
