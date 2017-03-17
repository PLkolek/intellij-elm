package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.OperatorSymbolRef;
import org.jetbrains.annotations.NotNull;


public abstract class ElmReference extends PsiReferenceBase.Poly<OperatorSymbolRef> {
    public ElmReference(OperatorSymbolRef element) {
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
}
