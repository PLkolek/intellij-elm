package mkolaczek.elm.references;

import com.google.common.collect.Sets;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ElmTypeConstructorRef;
import mkolaczek.elm.psi.node.ElmTypeExport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static mkolaczek.util.Optionals.stream;

public class ElmTypeConstructorReference extends PsiReferenceBase<ElmTypeConstructorRef> {
    public ElmTypeConstructorReference(ElmTypeConstructorRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }


    @Nullable
    @Override
    public PsiElement resolve() {
        ElmFile file = (ElmFile) myElement.getContainingFile();
        return file.typeDeclarations()
                   .stream()
                   .flatMap(decl -> decl.constructors().stream())
                   .filter(constructor -> myElement.getName().equals(constructor.getName()))
                   .findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        ElmTypeExport typeExport = PsiTreeUtil.getParentOfType(myElement, ElmTypeExport.class);
        assert typeExport != null;
        Set<String> excluded = Sets.newHashSet(typeExport.constructorNames());

        ElmFile file = (ElmFile) myElement.getContainingFile();
        return stream(file.typeDeclaration(typeExport.typeNameString()))
                .flatMap(decl -> decl.constructors().stream())
                .filter(elem -> !excluded.contains(elem.getName()))
                .toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

}