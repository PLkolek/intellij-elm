// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;
import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public class TypeDeclaration extends ASTWrapperPsiElement {

    public TypeDeclaration(ASTNode node) {
        super(node);
    }


    public Optional<String> typeNameString() {
        return Optional.ofNullable(findChildOfType(this, TypeName.class)).map(PsiElement::getText);
    }

    public Optional<TypeName> typeName() {
        return Optional.ofNullable(findChildOfType(this, TypeName.class));
    }

    public Collection<TypeConstructor> constructors() {
        return findChildrenOfType(this, TypeConstructor.class);
    }

    public boolean isAlias() {
        return PsiTreeUtil.findChildOfType(this, TypeAliasDeclNode.class) != null;
    }
}
