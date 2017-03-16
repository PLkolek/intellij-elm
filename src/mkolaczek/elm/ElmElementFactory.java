package mkolaczek.elm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiWhiteSpace;
import mkolaczek.elm.boilerplate.ElmFileType;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.Import;
import mkolaczek.elm.psi.node.Imports;
import mkolaczek.elm.psi.node.ModuleName;
import mkolaczek.elm.psi.node.ModuleNameRef;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ElmElementFactory {

    public static PsiElement typeName(Project project, String name) {
        ElmFile file = createFile(project, String.format("type %s = A", name));
        return file.module().typeDeclarations().iterator().next().getNameIdentifier();
    }

    public static PsiElement typeNameRef(Project project, String name) {
        ElmFile file = createFile(project, String.format("module Dummy exposing (%s)", name));
        return file.module().header().get().typeExport(name).get().typeName();
    }

    public static PsiElement typeConstructor(Project project, String name) {
        ElmFile file = createFile(project, String.format("type Dummy = %s)", name));
        return file.module().typeDeclaration("Dummy").get().constructors().iterator().next();
    }

    public static PsiElement typeConstructorRef(Project project, String name) {
        ElmFile file = createFile(project, String.format("module Dummy exposing (DummyType(%s))", name));
        return file.module().header().get().typeExport("DummyType").get().constructors().iterator().next();
    }

    public static ModuleName moduleName(Project project, String name) {
        ElmFile file = createFile(project, "module " + name);
        return file.module().header().get().moduleName();
    }

    public static ModuleNameRef moduleNameRef(Project project, String name) {
        ElmFile file = createFile(project, "import " + name);
        return file.module().imports().iterator().next().importedModuleName().get();
    }

    public static PsiElement moduleAlias(Project project, String name) {
        ElmFile file = createFile(project, "import Module as " + name);
        return file.module().imports().iterator().next().alias().get();
    }

    public static Import importLine(Project project, String importedModuleName) {
        return importsFile(project, importedModuleName).module().imports().iterator().next();
    }

    public static Imports imports(Project project, String importedModuleName) {
        return importsFile(project, importedModuleName).module().importsNode().get();
    }

    @NotNull
    private static ElmFile importsFile(Project project, String importedModuleName) {
        return createFile(project, "import " + importedModuleName + "\n");
    }

    public static PsiWhiteSpace newLine(Project project) {
        return newLines(project, 1);
    }

    public static PsiWhiteSpace newLines(Project project, int count) {
        ElmFile file = createFile(project, StringUtils.repeat("\n", count));
        return (PsiWhiteSpace) file.getChildren()[1];
    }

    private static ElmFile createFile(Project project, String text) {
        String name = "dummy.elm";
        return (ElmFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ElmFileType.INSTANCE, text);
    }
}
