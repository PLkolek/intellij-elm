// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ElmTypeDeclaration extends ASTWrapperPsiElement {

    public ElmTypeDeclaration(ASTNode node) {
        super(node);
    }


    public Optional<String> typeName() {
        return Optional.ofNullable(findChildOfType(this, ElmTypeName.class)).map(PsiElement::getText);
    }
}
