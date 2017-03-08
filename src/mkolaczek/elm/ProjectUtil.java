package mkolaczek.elm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import mkolaczek.elm.boilerplate.ElmFileType;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.Module;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class ProjectUtil {
    public static Stream<Module> modules(Project project) {
        return elmFiles(project).stream()
                                .map(PsiManager.getInstance(project)::findFile)
                                .map(ElmFile.class::cast)
                                .filter(Objects::nonNull)
                                .map(ElmFile::module)
                                .filter(Objects::nonNull);
    }

    @NotNull
    public static Collection<VirtualFile> elmFiles(Project project) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ElmFileType.INSTANCE, scope);
    }
}
