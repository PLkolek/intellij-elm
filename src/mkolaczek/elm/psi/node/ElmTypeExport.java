package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;
import java.util.List;

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

    public Collection<String> constructors() {
        ElmModuleValueList constructors = findChildOfType(this, ElmModuleValueList.class);

        return ofNullable(constructors).map(this::constructorNames).orElse(newArrayList());
    }

    private List<String> constructorNames(ElmModuleValueList list) {
        return list.values(ElmTypeConstructor.class)
                   .stream()
                   .map(PsiElement::getText)
                   .collect(toList());
    }
}
