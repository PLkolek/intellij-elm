// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.HasExposing;

import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class Import extends HasExposing {

    public Import(ASTNode node) {
        super(node);
    }

    public ModuleNameRef importedModule() {
        return findChildOfType(this, ModuleNameRef.class);
    }

    public Optional<String> alias() {
        return Optional.ofNullable(findChildOfType(this, ModuleAlias.class)).map(ModuleAlias::getText);
    }

    public boolean importedAs(String name) {
        if (alias().isPresent()) {
            return alias().get().equals(name);
        }
        return importedModule() != null && importedModule().getName().equals(name);
    }
}
