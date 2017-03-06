package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class ElmTypeExport extends ASTWrapperPsiElement {
    public ElmTypeExport(ASTNode node) {
        super(node);
    }

    public ElmTypeNameRef typeName() {
        return findChildOfType(this, ElmTypeNameRef.class);
    }

    public String typeNameString() {
        return PsiTreeUtil.firstChild(this).getText();
    }

    public boolean withoutConstructors() {
        return findChildOfType(this, ElmModuleValueList.class) == null;
    }

    public Collection<String> constructorNames() {
        return constructors().stream().map(ElmTypeConstructorRef::getName).collect(toList());
    }

    public Collection<ElmTypeConstructorRef> constructors() {
        ElmModuleValueList valueList = findChildOfType(this, ElmModuleValueList.class);
        return ofNullable(valueList).map(vl -> vl.values(ElmTypeConstructorRef.class)).orElse(newArrayList());
    }
}
