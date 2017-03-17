package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.SurroundContents;

public interface Surrounded extends PsiElement {

    default boolean isEmpty() {
        return PsiTreeUtil.getChildOfType(this, SurroundContents.class) == null;
    }
}
