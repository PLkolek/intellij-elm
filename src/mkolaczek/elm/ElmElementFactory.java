package mkolaczek.elm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiWhiteSpace;
import mkolaczek.elm.boilerplate.ElmFileType;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "ConstantConditions"})
public class ElmElementFactory {

    public static PsiElement typeName(Project project, String name) {
        ElmFile file = createFile(project, String.format("type %s = A", name));
        return file.module().declarations(TypeOfDeclaration.TYPE).iterator().next().getNameIdentifier();
    }

    public static PsiElement typeNameRef(Project project, String name) {
        ElmFile file = createFile(project, String.format("module Dummy exposing (%s)", name));
        return findChildOfType(file.module(), TypeExposing.class).typeName();
    }

    public static PsiElement typeConstructor(Project project, String name) {
        ElmFile file = createFile(project, String.format("type Dummy = %s)", name));
        return file.module()
                   .declarations(TypeOfDeclaration.TYPE, "Dummy")
                   .findAny()
                   .get()
                   .constructors()
                   .iterator()
                   .next();
    }

    public static PsiElement typeConstructorRef(Project project, String name) {
        ElmFile file = createFile(project, String.format("module Dummy exposing (DummyType(%s))", name));
        return findChildOfType(file.module(), TypeExposing.class).constructors().iterator().next();
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

    public static PsiElement operatorName(Project project, String name) {
        ElmFile file = createFile(project, String.format("(%s) x y = x + y", name));
        return file.module().declarations(TypeOfDeclaration.OPERATOR).findAny().get().getNameIdentifier();
    }

    public static PsiElement operatorNameRef(Project project, String newElementName) {
        ElmFile file = createFile(project, "module A exposing ((" + newElementName + "))");
        return findChildOfType(file.module(), OperatorSymbolRef.class);
    }

    public static PsiElement portName(Project project, String name) {
        ElmFile file = createFile(project, "port module M exposing(..)\nport " + name);
        return file.module().declarations(TypeOfDeclaration.PORT).findFirst().get().getNameIdentifier();
    }

    public static ValueExposing exposedValue(Project project, String name) {
        ElmFile file = createFile(project, String.format("module Dummy exposing (%s)", name));
        return findChildOfType(file.module(), ValueExposing.class);
    }

    public static PsiElement var(Project project, String name) {
        ElmFile file = createFile(project, String.format("x = %s", name));
        return findChildOfType(file.module(), Var.class);
    }

    public static PsiElement valueName(Project project, String name) {
        ElmFile file = createFile(project, String.format("%s = 5", name));
        return findChildOfType(file.module(), ValueName.class);
    }

    public static PsiElement valueNameRef(Project project, String name) {
        ElmFile file = createFile(project, String.format("%s : Int", name));
        return findChildOfType(file.module(), ValueNameRef.class);
    }

    private static ElmFile createFile(Project project, String text) {
        String name = "dummy.elm";
        return (ElmFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ElmFileType.INSTANCE, text);
    }
}
