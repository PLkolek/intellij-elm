package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.PsiUtil;
import mkolaczek.elm.psi.node.extensions.Declaration;

import java.util.stream.Stream;

public class Declarations extends ASTWrapperPsiElement {

    public Declarations(ASTNode node) {
        super(node);
    }

    public Stream<Declaration> declarations() {
        return PsiUtil.getChildrenOfType2(this, Declaration.class);
    }

}
