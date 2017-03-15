// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.extensions.HasExposing;

import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class Import extends HasExposing {

    public Import(ASTNode node) {
        super(node);
    }

    public Stream<Module> importedModule() {
        return ProjectUtil.modules(getProject())
                          .filter(m -> m.getName()
                                        .equals(importedModuleName().getName()));
    }

    public ModuleNameRef importedModuleName() {
        return findChildOfType(this, ModuleNameRef.class);
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
        if (aliasName().isPresent()) {
            return aliasName().get().equals(name);
        }
        return importedModuleName() != null && importedModuleName().getName().equals(name);
    }
}
