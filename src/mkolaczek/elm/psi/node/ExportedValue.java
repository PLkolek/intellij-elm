package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;


public class ExportedValue extends ASTWrapperPsiElement {
    public ExportedValue(ASTNode node) {
        super(node);
    }

    public Optional<TypeExport> typeExport() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, TypeExport.class));
    }

    public Optional<Operator> operatorExport() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, Operator.class));
    }

    @NotNull
    public CommaSeparatedList containingList() {
        return checkNotNull(PsiTreeUtil.getParentOfType(this, CommaSeparatedList.class));
    }
}
