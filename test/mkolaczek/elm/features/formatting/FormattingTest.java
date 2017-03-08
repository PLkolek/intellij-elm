package mkolaczek.elm.features.formatting;

import com.google.common.collect.Lists;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.boilerplate.ElmLanguage;

public class FormattingTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/formatting";
    }

    public void testFormatter() {
        myFixture.configureByFiles("Test.elm");
        CodeStyleSettingsManager.getSettings(getProject()).setRightMargin(ElmLanguage.INSTANCE, 60);

        new WriteCommandAction.Simple(getProject()) {
            @Override
            protected void run() throws Throwable {
                CodeStyleManager.getInstance(getProject()).reformatText(myFixture.getFile(), Lists.newArrayList(myFixture.getFile().getTextRange()));
            }
        }.execute();
        myFixture.checkResultByFile("expected.elm");
    }

}
