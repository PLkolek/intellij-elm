package mkolaczek.elm.references;

import com.google.common.collect.Sets;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Sets.newHashSet;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toSet;
import static mkolaczek.elm.psi.PsiUtil.*;
import static mkolaczek.elm.psi.node.Module.module;
import static mkolaczek.elm.psi.node.extensions.TypeOfDeclaration.TYPE;

public class TypeReference extends PsiReferenceBase.Poly<TypeNameRef> {
    public TypeReference(TypeNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()), false);
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

    public static TypeDeclaration[] variants(PsiElement myElement) {
        Set<String> excluded = Sets.newHashSet();

        TypeAliasDeclNode aliasDeclNode = getParentOfType(myElement, TypeAliasDeclNode.class);
        ModuleValueList exposingList = getParentOfType(myElement, ModuleValueList.class);
        if (aliasDeclNode != null) {
            String aliasName = aliasDeclNode.typeDeclaration().getName();
            excluded = newHashSet(aliasName);
        } else if (exposingList != null) {
            excluded = exposingList.exposed(TypeOfExposed.TYPE)
                                   .map(TypeExposing::exposedName)
                                   .collect(toSet());
        }

        Set<String> finalExcluded = excluded;

        return typeDeclarations(myElement, exposingList == null)
                .filter(elem -> !finalExcluded.contains(elem.getName()))
                .toArray(TypeDeclaration[]::new);
    }

    private static Stream<TypeDeclaration> typeDeclarations(PsiElement myElement, boolean includeImported) {
        QualifiedTypeNameRef qualifiedName = getParentOfType(myElement, QualifiedTypeNameRef.class);
        if (qualifiedName != null && qualifiedName.moduleName().isPresent()) {
            return module(myElement).imports(qualifiedName.moduleName().get().getName())
                                    .flatMap(TypeReference::moduleDecls);
        }

        Stream<TypeDeclaration> typeDecls = module(myElement).declarations(TypeOfDeclaration.TYPE);
        if (includeImported) {
            Stream<TypeDeclaration> imported = module(myElement).imports()
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
        Set<String> typeExports = i.exposed(TypeOfExposed.TYPE)
                                   .map(TypeExposing::exposedName)
                                   .collect(Collectors.toSet());
        return decls.filter(d -> typeExports.contains(d.getName()));
    }

    private static Stream<TypeDeclaration> moduleDecls(Import i) {
        return i.importedModule()
                .flatMap((module) -> module.declarations(TypeOfDeclaration.TYPE));
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Stream<? extends PsiNamedElement> resolved;
        if (insideModuleHeader(myElement)) {
            resolved = module(myElement).declarations(TYPE)
                                        .filter(d -> d.sameName(myElement.getName()));
        } else if (insideImport(myElement)) {
            resolved = containingImport(myElement).importedModule()
                                                  .flatMap(m -> m.exportedDeclaration(TYPE, myElement.getName()));
        } else {
            resolved = Resolver.forTypes().resolve(myElement);
        }
        return resolved.map(PsiElementResolveResult::new)
                       .toArray(ResolveResult[]::new);
    }
}
