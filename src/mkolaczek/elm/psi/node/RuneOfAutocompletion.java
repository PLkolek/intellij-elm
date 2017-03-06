package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import mkolaczek.elm.references.AutocompleteRuneReference;

public class RuneOfAutocompletion extends ASTWrapperPsiElement {
    public RuneOfAutocompletion(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new AutocompleteRuneReference(this);
    }

}
