package mkolaczek.elm.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.indexing.FileBasedIndex;
import mkolaczek.elm.ElmFileType;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmModuleNameRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class ElmModuleReference extends PsiReferenceBase<ElmModuleNameRef> {
    public ElmModuleReference(ElmModuleNameRef element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        Project project = myElement.getProject();

        return modules(project)
                .filter(module -> module.getName() != null && module.getName().equals(myElement.getName()))
                .findFirst().orElse(null);
    }

    private Stream<ElmModule> modules(Project project) {
        return elmFiles(project).stream()
                                .map(PsiManager.getInstance(project)::findFile)
                                .map(ElmFile.class::cast)
                                .filter(Objects::nonNull)
                                .map(file -> PsiTreeUtil.getChildOfType(file, ElmModule.class))
                                .filter(Objects::nonNull);
    }

    @NotNull
    private Collection<VirtualFile> elmFiles(Project project) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ElmFileType.INSTANCE, scope);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return modules(myElement.getProject()).toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
