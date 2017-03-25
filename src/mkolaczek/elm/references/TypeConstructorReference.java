package mkolaczek.elm.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mkolaczek.elm.psi.node.Module.module;

public class TypeConstructorReference extends PsiReferenceBase<PsiNamedElement> {
    public TypeConstructorReference(PsiNamedElement element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }


    @Nullable
    @Override
    public TypeConstructor resolve() {
        if (myElement.getName() == null) {
            return null;
        }
        return module(myElement).declarations(TypeOfDeclaration.TYPE)
                                .flatMap(TypeDeclaration::constructors)
                                .filter(constructor -> myElement.getName().equals(constructor.getName()))
                                .findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return PsiReference.EMPTY_ARRAY;
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

}