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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
        return modules(project).filter(module -> module.sameName(searchedModuleName));
    }

    public static Stream<String> otherModuleNames(Project project, Module module) {
        String currentModuleName = module.getName();
        List<String> moduleNames = modules(project).map(Module::getName).collect(toList());
        long modulesWithSameName = moduleNames.stream().filter(m -> m.equals(currentModuleName)).count();
        if (modulesWithSameName <= 1) {
            return moduleNames.stream().filter(n -> !n.equals(currentModuleName));
        }
        return moduleNames.stream();
    }
}
