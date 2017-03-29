package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;


abstract class ElmReference<T extends PsiNamedElement> extends PsiReferenceBase.Poly<T> {
    ElmReference(T element) {
        super(element, TextRange.create(0, element.getTextLength()), false);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return PsiReference.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return multiResolve()
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    protected abstract Stream<? extends PsiElement> multiResolve();
}
