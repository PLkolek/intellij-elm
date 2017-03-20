package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

public class CharacterLiteral extends ASTWrapperPsiElement {
    public CharacterLiteral(ASTNode node) {
        super(node);
    }
}
