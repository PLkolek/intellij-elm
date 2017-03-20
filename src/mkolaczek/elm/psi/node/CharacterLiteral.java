package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import mkolaczek.elm.psi.Tokens;

public class CharacterLiteral extends ASTWrapperPsiElement {
    public CharacterLiteral(ASTNode node) {
        super(node);
    }

    public boolean isLengthValid() {
        ASTNode[] children = getNode().getChildren(TokenSet.ANY);
        return children.length == 3 && isOneCharacter(children[1]);
    }

    private boolean isOneCharacter(ASTNode node) {
        return node.getElementType() != Tokens.STRING_CONTENT || node.getText().length() == 1;
    }
}
