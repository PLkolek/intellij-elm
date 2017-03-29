package mkolaczek.elm.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.ModuleNameRef;

import java.util.Optional;
import java.util.stream.Stream;

public class ModuleResolver {

    public static Stream<PsiElement> resolveToAlias(ModuleNameRef target) {
        Optional<Stream<Module>> inImport = inImport(target);
        if (inImport.isPresent()) {
            return inImport.get().map(x -> x);
        }
        return Stream.empty();
    }

    public static Stream<Module> resolveToModule(ModuleNameRef target) {
        Optional<Stream<Module>> inImport = inImport(target);
        if (inImport.isPresent()) {
            return inImport.get();
        }
        return Stream.empty();
    }

    private static Optional<Stream<Module>> inImport(ModuleNameRef target) {
        if (PsiTreeUtil.getParentOfType(target, Import.class) != null) {
            Stream<Module> result = ProjectUtil.modules(target.getProject(), target.getName());
            return Optional.of(result);
        }
        return Optional.empty();
    }

}
