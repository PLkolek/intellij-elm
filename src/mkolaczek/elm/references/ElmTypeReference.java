package mkolaczek.elm.references;

import com.google.common.collect.Sets;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.*;
import mkolaczek.util.Optionals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

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
        Set<String> excluded = Sets.newHashSet();

        ElmTypeAliasDeclNode aliasDeclNode = PsiTreeUtil.getParentOfType(myElement, ElmTypeAliasDeclNode.class);
        if (aliasDeclNode != null) {
            String aliasName = aliasDeclNode.typeDeclaration().typeName().map(ElmTypeName::getName).orElse("");
            excluded = newHashSet(aliasName);
        } else {
            ElmModuleValueList exposingList = PsiTreeUtil.getParentOfType(myElement, ElmModuleValueList.class);
            if (exposingList != null) {
                excluded = exposingList.exportedTypes()
                                       .stream()
                                       .map(ElmTypeExport::typeNameString)
                                       .collect(toSet());
            }
        }

        ElmFile file = (ElmFile) myElement.getContainingFile();
        Set<String> finalExcluded = excluded;
        return file.typeDeclarations()
                   .stream()
                   .map(ElmTypeDeclaration::typeName)
                   .flatMap(Optionals::stream).filter(elem -> !finalExcluded.contains(elem.getName()))
                   .toArray();
    }
}
