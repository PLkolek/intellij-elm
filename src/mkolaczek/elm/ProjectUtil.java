package mkolaczek.elm;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import mkolaczek.elm.boilerplate.ElmFileType;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.extensions.ElmNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class ProjectUtil {
    public static Stream<Module> modules(Project project) {
        return elmFiles(project).map(PsiManager.getInstance(project)::findFile)
                                .map(ElmFile.class::cast)
                                .filter(Objects::nonNull)
                                .map(ElmFile::module)
                                .filter(Objects::nonNull);
    }

    @NotNull
    public static Stream<VirtualFile> elmFiles(Project project) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ElmFileType.INSTANCE, scope)
                             .stream()
                             .filter(f -> !isPackageTestFile(f));
    }

    private static boolean isPackageTestFile(VirtualFile f) {
        return f.getPath().contains("/elm-stuff/") && f.getPath().contains("/tests/");
    }

    public static Stream<Module> modules(Project project, String searchedModuleName) {
        if (Strings.isNullOrEmpty(searchedModuleName)) {
            return Stream.empty();
        }
        return modules(project, ImmutableSet.of(searchedModuleName));
    }

    public static Stream<Module> modules(Project project, Set<String> searchedModuleNames) {
        return modules(project).filter(ElmNamedElement.nameIn(searchedModuleNames));
    }

}
