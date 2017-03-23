package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.PsiElement;
import mkolaczek.elm.psi.node.ModuleNameRef;

import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public interface QualifiedRef extends PsiElement {
    default Optional<ModuleNameRef> moduleName() {
        return Optional.ofNullable(findChildOfType(this, ModuleNameRef.class));
    }
}
