package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.DocComment;

import java.util.Optional;

public interface DocCommented extends PsiElement {
    Optional<DocComment> docComment();
}
