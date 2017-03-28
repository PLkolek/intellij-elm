package mkolaczek.elm.features.inspections;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiFile;
import com.intellij.util.ObjectUtils;
import mkolaczek.elm.psi.node.Module;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

class ModulePopupStep extends BaseListPopupStep<Module> {
    private final AddImportIntentionAction addImportIntentionAction;
    private final Project project;
    private final PsiFile file;

    public ModulePopupStep(AddImportIntentionAction addImportIntentionAction,
                           List<Module> modules,
                           Project project,
                           PsiFile file) {
        super("Choose module to import", modules);
        this.addImportIntentionAction = addImportIntentionAction;
        this.project = project;
        this.file = file;
    }

    @Override
    public boolean isAutoSelectionEnabled() {
        return false;
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return true;
    }

    @Override
    public PopupStep onChosen(Module selectedValue, boolean finalChoice) {
        if (selectedValue == null) {
            return FINAL_CHOICE;
        }

        return doFinalStep(() -> addImportIntentionAction.executeCommand(project, file, selectedValue.getName()));
    }

    @Override
    public boolean hasSubstep(Module selectedValue) {
        return false;
    }

    @NotNull
    @Override
    public String getTextFor(Module value) {
        return ObjectUtils.assertNotNull(value.getName());
    }

    @Override
    public Icon getIconFor(Module aValue) {
        return aValue.getIcon(0);
    }
}
