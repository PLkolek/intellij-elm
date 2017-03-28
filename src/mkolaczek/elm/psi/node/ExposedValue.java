package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.extensions.Exposed;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;


public class ExposedValue extends ASTWrapperPsiElement {
    public ExposedValue(ASTNode node) {
        super(node);
    }

    public <T extends Exposed> Optional<T> exposed(TypeOfExposed<T> exposedElementsType) {
        return Optional.ofNullable(findChildOfType(this, exposedElementsType.psiClass()));
    }

    @NotNull
    public CommaSeparatedList containingList() {
        return checkNotNull(PsiTreeUtil.getParentOfType(this, CommaSeparatedList.class));
    }
}
