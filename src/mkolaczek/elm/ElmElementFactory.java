package mkolaczek.elm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import mkolaczek.elm.boilerplate.ElmFileType;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ModuleName;
import mkolaczek.elm.psi.node.ModuleNameRef;

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
        return file.module().imports().iterator().next().importedModuleName();
    }

    public static PsiElement moduleAlias(Project project, String name) {
        ElmFile file = createFile(project, "import Module as " + name);
        return file.module().imports().iterator().next().alias().get();
    }

    private static ElmFile createFile(Project project, String text) {
        String name = "dummy.elm";
        return (ElmFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ElmFileType.INSTANCE, text);
    }
}
