package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.node.extensions.Declaration;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfixDeclaration extends ElmNamedElement implements Declaration {
    public InfixDeclaration(ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    protected PsiElement createNewNameIdentifier(@NonNls @NotNull String name) {
        return ElmElementFactory.operatorNameRef(getProject(), name);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        //required for ctrl+click find usages to work
        return PsiTreeUtil.findChildOfType(this, OperatorSymbolRef.class);
    }
}
