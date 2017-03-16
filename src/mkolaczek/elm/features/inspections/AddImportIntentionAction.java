package mkolaczek.elm.features.inspections;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.intention.AbstractIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import mkolaczek.elm.ElmElementFactory;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.Declarations;
import mkolaczek.elm.psi.node.Imports;
import mkolaczek.elm.psi.node.Module;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static mkolaczek.elm.ProjectUtil.modules;

public class AddImportIntentionAction extends AbstractIntentionAction {

    private final String moduleNameToImport;

    public AddImportIntentionAction(String moduleNameToImport) {
        this.moduleNameToImport = moduleNameToImport;
    }

    @Nls
    @NotNull
    @Override
    public String getText() {
        return "Import module";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        List<Module> modules = modules(project, moduleNameToImport).collect(toList());
        if (modules.size() == 0) {
            HintManager.getInstance().showErrorHint(editor, "No module called'" + moduleNameToImport + "' found");
        } else if (modules.size() == 1) {
            addImport(project, file, modules.get(0).getName());
        } else {

        }
    }

    private void addImport(Project project, PsiFile file, String importedModuleName) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            Module importingModule = ((ElmFile) file).module();
            Optional<Imports> imports = importingModule.importsNode();
            if (imports.isPresent()) {
                addImportToImports(project, imports.get(), importedModuleName);
            } else {
                Optional<Declarations> declarations = importingModule.declarationsNode();
                Imports newImports = ElmElementFactory.imports(project, importedModuleName);
                if (declarations.isPresent()) {
                    addImportsBeforeDeclarations(project, importingModule, declarations.get(), newImports);
                } else {
                    addImportsToEmptyFile(project, importingModule, newImports);
                }
            }

        });
    }

    private void addImportsToEmptyFile(Project project, Module importingModule, Imports newImports) {
        importingModule.add(ElmElementFactory.newLine(project));
        importingModule.add(newImports);
        importingModule.add(ElmElementFactory.newLines(project, 3));
    }

    private void addImportsBeforeDeclarations(Project project,
                                              Module importingModule,
                                              Declarations declarations, Imports newImports) {
        importingModule.addBefore(ElmElementFactory.newLine(project), declarations);
        importingModule.addBefore(newImports, declarations);
        importingModule.addBefore(ElmElementFactory.newLines(project, 3), declarations);
    }

    private void addImportToImports(Project project, Imports imports, String importedModuleName) {
        PsiElement afterImports = imports.getNextSibling();
        imports.add(ElmElementFactory.newLine(project));
        imports.add(ElmElementFactory.importLine(project, importedModuleName));
        if (!afterImports.getText().startsWith("\n")) {
            imports.add(ElmElementFactory.newLine(project));
        }
    }


}
