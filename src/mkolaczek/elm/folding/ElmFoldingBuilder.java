package mkolaczek.elm.folding;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.Elements;
import mkolaczek.elm.psi.node.ModuleHeader;
import mkolaczek.elm.psi.node.TypeDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static mkolaczek.elm.psi.Elements.*;

public class ElmFoldingBuilder implements FoldingBuilder {

    private static final Set<IElementType> FOLDABLE_NODES = Sets.newHashSet(
            DOC_COMMENT,
            MULTILINE_COMMENT,
            IMPORTS,
            EXPOSING_NODE,
            EFFECT_MODULE_SETTINGS,
            TYPE_DECLARATION);

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = Lists.newArrayList();
        collectDescriptors(node, document, descriptors);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    private void collectDescriptors(ASTNode node, Document document, List<FoldingDescriptor> descriptors) {
        if (FOLDABLE_NODES.contains(node.getElementType()) && spanMultipleLines(node, document)) {
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
        if (type == DOC_COMMENT) {
            return "{-|...-}";
        }
        if (type == Elements.MULTILINE_COMMENT) {
            return "{-...-}";
        }
        if (type == IMPORTS) {
            return "import ...";
        }
        if (type == EXPOSING_NODE) {
            return "exposing ...";
        }
        if (type == EFFECT_MODULE_SETTINGS) {
            return "where ...";
        }
        if (type == TYPE_DECLARATION) {
            TypeDeclaration decl = (TypeDeclaration) node.getPsi();
            return "type " + (decl.isAlias() ? "alias " : "") + decl.typeNameString().orElse("") + " = ...";
        }
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        IElementType type = node.getElementType();
        if (type == IMPORTS || type == EFFECT_MODULE_SETTINGS) {
            return true;
        }
        //noinspection RedundantIfStatement
        if (type == EXPOSING_NODE && PsiTreeUtil.getParentOfType(node.getPsi(), ModuleHeader.class) != null) {
            return true;
        }

        return false;
    }

    private static boolean spanMultipleLines(@NotNull ASTNode node, @NotNull Document document) {
        final TextRange range = node.getTextRange();
        return document.getLineNumber(range.getStartOffset()) < document.getLineNumber(range.getEndOffset());
    }
}
