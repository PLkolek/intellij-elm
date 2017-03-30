package mkolaczek.elm;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import mkolaczek.elm.boilerplate.ElmIcon;
import mkolaczek.elm.features.refactoring.rename.InplaceRenamer;

public class CreateElmModuleAction extends CreateFileFromTemplateAction {
    private static final String ELM_TEMPLATE = "ElmModule";

    public CreateElmModuleAction() {
        super("Elm module", "Create new Elm module", ElmIcon.FILE);
    }

    @Override
    protected String getDefaultTemplateProperty() {
        return ELM_TEMPLATE;
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle("Create new Elm module").addKind("Elm module", ElmIcon.FILE, "ElmModule");
        builder.setValidator(new InputValidatorEx() {
            @Override
            public String getErrorText(String inputString) {
                if (inputString.length() > 0 && !InplaceRenamer.moduleNameOk(inputString)) {
                    return "This is not a valid Elm module name";
                }
                return null;
            }

            @Override
            public boolean checkInput(String inputString) {
                return true;
            }

            @Override
            public boolean canClose(String inputString) {
                return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
            }
        });
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "New Elm module";
    }
}
