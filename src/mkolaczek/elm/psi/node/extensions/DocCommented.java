package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import mkolaczek.elm.psi.node.DocComment;

import java.util.Optional;

public interface DocCommented extends PsiElement {
    default Optional<DocComment> docComment() {
        PsiElement prevSibling = getPrevSibling();
        while (prevSibling instanceof PsiWhiteSpace) {
            prevSibling = prevSibling.getPrevSibling();
        }
        return prevSibling instanceof DocComment ? Optional.of((DocComment) prevSibling) : Optional.empty();

    }
}
