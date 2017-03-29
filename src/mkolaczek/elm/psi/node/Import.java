// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.builtInImports.AbstractImport;
import mkolaczek.elm.psi.node.extensions.PsiHasExposing;

import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class Import extends ASTWrapperPsiElement implements PsiHasExposing, AbstractImport {

    public Import(ASTNode node) {
        super(node);
    }

    public Optional<ModuleNameRef> importedModuleName() {
        return Optional.ofNullable(findChildOfType(this, ModuleNameRef.class));
    }

    @Override
    public Optional<String> moduleNameString() {
        return importedModuleName().map(PsiNamedElement::getName);
    }

    public boolean isAliased() {
        return aliasName().isPresent();
    }

    public Optional<String> aliasName() {
        return Optional.ofNullable(findChildOfType(this, ModuleAlias.class)).map(ModuleAlias::getName);
    }

    public Optional<ModuleAlias> alias() {
        return Optional.ofNullable(findChildOfType(this, ModuleAlias.class));
    }

    public boolean importedAs(String name) {
        return importedAs().map(n -> n.equals(name)).orElse(false);
    }

    public Optional<String> importedAs() {
        if (aliasName().isPresent()) {
            return aliasName();
        }
        return moduleNameString();
    }

    @Override
    public boolean noExposingExposesAll() {
        return false;
    }
}
