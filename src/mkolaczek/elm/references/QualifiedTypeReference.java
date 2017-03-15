package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.TypeNameRef;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QualifiedTypeReference extends PsiReferenceBase<TypeNameRef> {


    public QualifiedTypeReference(@NotNull TypeNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return new TypeReference(myElement).resolve();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ArrayUtils.addAll(TypeReference.variants(myElement), ModuleReference.variants(myElement));
    }
}
