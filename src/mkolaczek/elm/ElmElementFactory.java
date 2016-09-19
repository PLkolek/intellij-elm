package mkolaczek.elm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ElmModuleName;
import mkolaczek.elm.psi.node.ElmModuleNameRef;

public class ElmElementFactory {

    public static ElmModuleName moduleName(Project project, String name) {
        ElmFile file = createFile(project, "module " + name);
        return file.header().moduleName();
    }

    public static ElmModuleNameRef moduleNameRef(Project project, String name) {
        ElmFile file = createFile(project, "import " + name);
        return file.imports().iterator().next().importedModule();
    }

    private static ElmFile createFile(Project project, String text) {
        String name = "dummy.elm";
        return (ElmFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ElmFileType.INSTANCE, text);
    }
}
