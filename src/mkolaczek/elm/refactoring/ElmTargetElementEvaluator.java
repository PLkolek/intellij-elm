package mkolaczek.elm.refactoring;


import com.google.common.collect.Maps;
import com.intellij.codeInsight.TargetElementEvaluatorEx2;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.ModuleName;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.TypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class ElmTargetElementEvaluator extends TargetElementEvaluatorEx2 {

    private static final Map<Class<? extends PsiElement>, Class<? extends PsiElement>> nameToElement = Maps.newHashMap();

    static {
        nameToElement.put(ModuleName.class, Module.class);
        nameToElement.put(TypeName.class, TypeDeclaration.class);
    }

    @Nullable
    @Override
    public PsiElement getElementByReference(@NotNull PsiReference ref, int flags) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getNamedElement(@NotNull PsiElement element) {
        if (element instanceof LeafPsiElement) {
            LeafPsiElement leaf = (LeafPsiElement) element;
            for (Map.Entry<Class<? extends PsiElement>, Class<? extends PsiElement>> entry : nameToElement.entrySet()) {
                if (getParentOfType(leaf, entry.getKey()) != null) {
                    return getParentOfType(element, entry.getValue());
                }
            }
        }
        return super.getNamedElement(element);
    }
}
