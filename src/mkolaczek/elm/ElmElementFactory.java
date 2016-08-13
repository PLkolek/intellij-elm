package mkolaczek.elm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ElmModuleName;

public class ElmElementFactory {

    public static ElmModuleName moduleName(Project project, String name) {
        final ElmFile file = createFile(project, "module " + name);
        return (ElmModuleName) file.getFirstChild().getNextSibling().getNextSibling();
    }

    public static ElmFile createFile(Project project, String text) {
        String name = "dummy.elm";
        return (ElmFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ElmFileType.INSTANCE, text);
    }
}
