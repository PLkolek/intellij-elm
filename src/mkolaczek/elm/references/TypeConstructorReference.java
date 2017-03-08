package mkolaczek.elm.references;

import com.google.common.collect.Sets;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeConstructorRef;
import mkolaczek.elm.psi.node.TypeExport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static mkolaczek.elm.psi.node.Module.module;
import static mkolaczek.util.Streams.stream;

public class TypeConstructorReference extends PsiReferenceBase<TypeConstructorRef> {
    public TypeConstructorReference(TypeConstructorRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }


    @Nullable
    @Override
    public TypeConstructor resolve() {
        return module(myElement).typeDeclarations()
                                .stream()
                                .flatMap(decl -> decl.constructors().stream())
                                .filter(constructor -> myElement.getName().equals(constructor.getName()))
                                .findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        TypeExport typeExport = PsiTreeUtil.getParentOfType(myElement, TypeExport.class);
        assert typeExport != null;
        Set<String> excluded = Sets.newHashSet(typeExport.constructorNames());

        return stream(module(myElement).typeDeclaration(typeExport.typeNameString()))
                .flatMap(decl -> decl.constructors().stream())
                .filter(elem -> !excluded.contains(elem.getName()))
                .toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

}