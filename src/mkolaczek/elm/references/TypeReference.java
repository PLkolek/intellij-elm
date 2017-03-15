package mkolaczek.elm.references;

import com.google.common.collect.Sets;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Sets.newHashSet;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.psi.node.Module.module;

public class TypeReference extends PsiReferenceBase<TypeNameRef> {
    public TypeReference(TypeNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }


    @Nullable
    @Override
    public TypeDeclaration resolve() {
        return typeDeclarations(myElement, true)
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

        TypeAliasDeclNode aliasDeclNode = getParentOfType(myElement, TypeAliasDeclNode.class);
        ModuleValueList exposingList = getParentOfType(myElement, ModuleValueList.class);
        if (aliasDeclNode != null) {
            String aliasName = aliasDeclNode.typeDeclaration().getName();
            excluded = newHashSet(aliasName);
        } else if (exposingList != null) {
            excluded = exposingList.exportedTypes()
                                   .stream()
                                   .map(TypeExport::typeNameString)
                                   .collect(toSet());
        }

        Set<String> finalExcluded = excluded;

        return typeDeclarations(myElement, exposingList == null)
                .filter(elem -> !finalExcluded.contains(elem.getName()))
                .toArray();
    }

    private static Stream<TypeDeclaration> typeDeclarations(PsiElement myElement, boolean includeImported) {
        QualifiedTypeNameRef qualifiedName = getParentOfType(myElement, QualifiedTypeNameRef.class);
        if (qualifiedName != null && qualifiedName.moduleName() != null) {
            return module(myElement).imports(qualifiedName.moduleName().getName())
                                    .flatMap(TypeReference::moduleDecls);
        }

        Stream<TypeDeclaration> typeDecls = module(myElement).typeDeclarations()
                                                             .stream();
        if (includeImported) {
            Stream<TypeDeclaration> imported = module(myElement).imports()
                                                                .stream()
                                                                .flatMap(TypeReference::importedTypes);
            typeDecls = Stream.concat(typeDecls, imported);
        }
        return typeDecls;
    }

    private static Stream<TypeDeclaration> importedTypes(Import i) {
        if (i.importedModuleName() == null) {
            return Stream.empty();
        }

        Stream<TypeDeclaration> decls = moduleDecls(i);
        if (i.exposingList().isPresent() && i.exposingList().get().isOpenListing()) {
            return decls;
        }
        Set<String> typeExports = i.typeExports()
                                   .stream()
                                   .map(TypeExport::typeNameString)
                                   .collect(Collectors.toSet());
        return decls.filter(d -> typeExports.contains(d.getName()));
    }

    private static Stream<TypeDeclaration> moduleDecls(Import i) {
        return i.importedModule()
                .flatMap(m -> m.typeDeclarations().stream());
    }


}
