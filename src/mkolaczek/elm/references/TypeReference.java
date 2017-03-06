package mkolaczek.elm.references;

import com.google.common.collect.Sets;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeReference extends PsiReferenceBase<TypeNameRef> {
    public TypeReference(TypeNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }


    @Nullable
    @Override
    public TypeDeclaration resolve() {
        return module(myElement).typeDeclarations()
                                .stream()
                                .filter(type -> myElement.getName().equals(type.getName()))
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

        TypeAliasDeclNode aliasDeclNode = PsiTreeUtil.getParentOfType(myElement, TypeAliasDeclNode.class);
        if (aliasDeclNode != null) {
            String aliasName = aliasDeclNode.typeDeclaration().getName();
            excluded = newHashSet(aliasName);
        } else {
            ModuleValueList exposingList = PsiTreeUtil.getParentOfType(myElement, ModuleValueList.class);
            if (exposingList != null) {
                excluded = exposingList.exportedTypes()
                                       .stream()
                                       .map(TypeExport::typeNameString)
                                       .collect(toSet());
            }
        }

        Set<String> finalExcluded = excluded;
        return module(myElement).typeDeclarations()
                                .stream()
                                .filter(elem -> !finalExcluded.contains(elem.getName()))
                                .toArray();
    }
}
