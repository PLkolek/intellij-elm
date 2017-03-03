// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

import java.util.Collection;
import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class ElmTypeDeclaration extends ASTWrapperPsiElement {

    public ElmTypeDeclaration(ASTNode node) {
        super(node);
    }


    public Optional<String> typeNameString() {
        return Optional.ofNullable(findChildOfType(this, ElmTypeName.class)).map(PsiElement::getText);
    }

    public Optional<ElmTypeName> typeName() {
        return Optional.ofNullable(findChildOfType(this, ElmTypeName.class));
    }

    public Collection<ElmTypeConstructor> constructors() {
        return findChildrenOfType(this, ElmTypeConstructor.class);
    }
}
