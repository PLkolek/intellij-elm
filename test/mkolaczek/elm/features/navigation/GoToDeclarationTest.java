package mkolaczek.elm.features.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

public class GoToDeclarationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }

    public void testModuleDeclaration() {
        doTest("module");
    }

    public void testOperatorDeclaration() {
        doTest("operator");
    }

    private void doTest(String dir) {
        PsiFile[] files = myFixture.configureByFiles(dir + "/Test1.elm", dir + "/Test2.elm");
        //com.intellij.openapi.fileEditor.impl.TestEditorManagerImpl.openEditor() returns empty list,
        //which prevents GoToDeclarationAction from opening new editor, so here I do it manually
        Editor oldEditor = getEditor();
        myFixture.openFileInEditor(files[1].getVirtualFile());
        new GotoDeclarationAction().invoke(getProject(), oldEditor, files[0]);

        myFixture.checkResultByFile(dir + "/Test2.expected.elm");
    }
}
