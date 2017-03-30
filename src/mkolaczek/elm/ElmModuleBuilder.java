package mkolaczek.elm;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import mkolaczek.elm.boilerplate.ElmIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ElmModuleBuilder extends JavaModuleBuilder {

    public ElmModuleBuilder() {
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
        /*if (ProjectJdkTable.getInstance().getSdksOfType(RsSdkType.getInstance()).isEmpty()) {
            return "<html><body>Before you start make sure you have Redline Smalltalk installed." +
                    "<br/>Download <a href='https://github.com/redline-smalltalk/redline-smalltalk.github.com/raw/master/assets/redline-deploy.zip'>the latest version</a>" +
                    "<br/>Unpack the zip file to any folder and select it as Redline SDK</body></html>";
        } else {*/
        return "Elm module";
        //}
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

}
