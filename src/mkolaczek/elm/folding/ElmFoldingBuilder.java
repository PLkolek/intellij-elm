package mkolaczek.elm.folding;

import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.ElmElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElmFoldingBuilder implements FoldingBuilder {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = Lists.newArrayList();
        collectDescriptors(node, document, descriptors);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    private void collectDescriptors(ASTNode node, Document document, List<FoldingDescriptor> descriptors) {
        final IElementType type = node.getElementType();
        if (type == ElmElementTypes.DOC_COMMENT || type == ElmElementTypes.MULTILINE_COMMENT) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }

        for (ASTNode child : node.getChildren(null)) {
            collectDescriptors(child, document, descriptors);
        }
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        final IElementType type = node.getElementType();
        if(type == ElmElementTypes.DOC_COMMENT) {
            return "{-|...-}";
        }
        if(type == ElmElementTypes.MULTILINE_COMMENT) {
            return "{-...-}";
        }
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
