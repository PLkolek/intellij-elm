package mkolaczek.elm.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ProjectUtil;
import mkolaczek.elm.psi.node.ModuleNameRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mkolaczek.elm.psi.node.Module.module;

public class ImportModuleReference extends PsiReferenceBase<ModuleNameRef> {
    public ImportModuleReference(ModuleNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        Project project = myElement.getProject();

        return ProjectUtil.modules(project)
                          .filter(module -> module.sameName(myElement.getName()))
                          .findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return variants(myElement);
    }

    public static Object[] variants(PsiElement myElement) {
        return ProjectUtil.modules(myElement.getProject())
                          .filter(module -> !module(myElement).sameName(module))
                          .toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
