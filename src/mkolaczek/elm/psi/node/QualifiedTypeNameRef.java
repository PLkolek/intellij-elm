package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class QualifiedTypeNameRef extends ASTWrapperPsiElement {
    public QualifiedTypeNameRef(ASTNode node) {
        super(node);
    }

    public Optional<ModuleNameRef> moduleName() {
        return Optional.ofNullable(findChildOfType(this, ModuleNameRef.class));
    }
}
