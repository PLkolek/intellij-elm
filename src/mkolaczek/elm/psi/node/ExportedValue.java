package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.extensions.TypeOfExport;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;


public class ExportedValue extends ASTWrapperPsiElement {
    public ExportedValue(ASTNode node) {
        super(node);
    }

    public <T extends PsiElement> Optional<T> export(TypeOfExport<T> exposedElementsType) {
        return Optional.ofNullable(findChildOfType(this, exposedElementsType.psiClass()));
    }

    @NotNull
    public CommaSeparatedList containingList() {
        return checkNotNull(PsiTreeUtil.getParentOfType(this, CommaSeparatedList.class));
    }
}
