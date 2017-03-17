package mkolaczek.elm.features.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class GoToDeclarationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/navigation";
    }

    public void testModuleDeclaration() {
        PsiFile[] files = myFixture.configureByFiles("Test1.elm", "Test2.elm");
        //com.intellij.openapi.fileEditor.impl.TestEditorManagerImpl.openEditor() returns empty list,
        //which prevents GoToDeclarationAction from opening new editor, so here I do it manually
        Editor oldEditor = getEditor();
        myFixture.openFileInEditor(files[1].getVirtualFile());
        new GotoDeclarationAction().invoke(getProject(), oldEditor, files[0]);

        myFixture.checkResultByFile("Test2.expected.elm");
    }
}
