// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.Tokens;

public class DocComment extends ASTWrapperPsiElement implements PsiComment {

    public DocComment(ASTNode node) {
        super(node);
    }

    @Override
    public IElementType getTokenType() {
        return Tokens.BEGIN_DOC_COMMENT;
    }
}