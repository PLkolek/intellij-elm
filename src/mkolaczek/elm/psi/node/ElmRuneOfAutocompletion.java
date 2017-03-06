package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.references.ElmAutocompleteRuneReference;

public class ElmRuneOfAutocompletion extends ASTWrapperPsiElement {
    public ElmRuneOfAutocompletion(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new ElmAutocompleteRuneReference(this);
    }

}
