package mkolaczek.elm;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import mkolaczek.elm.boilerplate.ElmIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ElmModuleBuilder extends JavaModuleBuilder implements ModuleBuilderListener {

    public ElmModuleBuilder() {
        addListener(this);
    }

    @Override
    public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof ElmSdkType;
    }

    @Override
    public String getBuilderId() {
        return "elm";
    }

    @Override
    public Icon getBigIcon() {
        return null;
    }

    @Override
    public Icon getNodeIcon() {
        return ElmIcon.FILE;
    }

    @Override
    public String getDescription() {
        if (ProjectJdkTable.getInstance().getSdksOfType(ElmSdkType.getInstance()).isEmpty()) {
            return "<html><body>Before you start make sure you have Elm installed." +
                    "</body></html>";
        } else {
            return "Elm module";
        }
    }

    @Override
    public String getPresentableName() {
        return "Elm Module";
    }

    @Override
    public String getGroupName() {
        return "Elm";
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{};
    }

    @Override
    public void moduleCreated(@NotNull Module module) {
        Sdk sdk = myJdk == null ? ProjectRootManager.getInstance(module.getProject()).getProjectSdk() : myJdk;
        ElmSdkType.prepareModule(sdk, module);
    }
}
