package mkolaczek.elm.psi.node;

import com.google.common.base.Joiner;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class TypeExport extends ASTWrapperPsiElement {
    public TypeExport(ASTNode node) {
        super(node);
    }

    public TypeNameRef typeName() {
        return findChildOfType(this, TypeNameRef.class);
    }

    public String typeNameString() {
        return PsiTreeUtil.firstChild(this).getText();
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


    //WEIRD STUFF
    public static String declarationString(TypeExport typeExport) {
        return typeExport.typeNameString() + " = " + Joiner.on(" | ").join(typeExport.constructorNames());
    }
}
