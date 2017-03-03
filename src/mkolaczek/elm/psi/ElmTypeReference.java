package mkolaczek.elm.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.ElmTypeDeclaration;
import mkolaczek.elm.psi.node.ElmTypeNameRef;
import mkolaczek.util.Optionals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElmTypeReference extends PsiReferenceBase<ElmTypeNameRef> {
    public ElmTypeReference(ElmTypeNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }


    @Nullable
    @Override
    public PsiElement resolve() {
        ElmFile file = (ElmFile) myElement.getContainingFile();
        return file.typeDeclarations()
                   .stream()
                   .map(ElmTypeDeclaration::typeName)
                   .flatMap(Optionals::stream)
                   .filter(type -> myElement.getText().equals(type.getText()))
                   .findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return variants(myElement);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    public static Object[] variants(PsiElement myElement) {
        ElmFile file = (ElmFile) myElement.getContainingFile();
        return file.typeDeclarations()
                   .stream()
                   .map(ElmTypeDeclaration::typeName)
                   .flatMap(Optionals::stream)
                   .toArray();
    }
}
