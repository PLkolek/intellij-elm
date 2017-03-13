package mkolaczek.elm.psi.node.extensions;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.SurroundContents;
import org.jetbrains.annotations.NotNull;

public abstract class Surrounded extends ASTWrapperPsiElement {

    public Surrounded(@NotNull ASTNode node) {
        super(node);
    }

    public boolean isEmpty() {
        return PsiTreeUtil.getChildOfType(this, SurroundContents.class) == null;
    }
}
