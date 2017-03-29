package mkolaczek.elm.psi.node;

import com.google.common.base.Joiner;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.PsiExposed;

import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class TypeExposing extends ASTWrapperPsiElement implements PsiExposed {
    public TypeExposing(ASTNode node) {
        super(node);
    }

    public TypeNameRef typeName() {
        return findChildOfType(this, TypeNameRef.class);
    }

    public boolean withoutConstructors() {
        return findChildOfType(this, ModuleValueList.class) == null;
    }

    public Collection<String> constructorNames() {
        return constructors().stream().map(TypeConstructorRef::getName).collect(toList());
    }

    public Collection<TypeConstructorRef> constructors() {
        CommaSeparatedList valueList = findChildOfType(this, CommaSeparatedList.class);
        return ofNullable(valueList).map(vl -> vl.values(TypeConstructorRef.class)).orElse(newArrayList());
    }

    public Optional<ModuleValueList> valueList() {
        return Optional.ofNullable(findChildOfType(this, ModuleValueList.class));
    }

    public boolean exposesEverything() {
        return valueList().map(ModuleValueList::isOpenListing).orElse(false);
    }

    @Override
    public boolean exposes(String constructorName) {
        return exposesEverything() || constructorNames().contains(constructorName);
    }

    //WEIRD STUFF
    public static String declarationString(TypeExposing typeExposing) {
        return typeExposing.exposedName() + " = " + Joiner.on(" | ").join(typeExposing.constructorNames());
    }

    @Override
    public String exposedName() {
        if (typeName() != null) {
            return typeName().getName();
        }
        return null;
    }
}
