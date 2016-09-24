package mkolaczek.elm.navigation;

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
        //perform go to by hand (see com.intellij.codeInsight.navigation.GotoDeclarationTest
        new GotoDeclarationAction().invoke(getProject(), oldEditor, files[0]);
        /*PsiElement element = GotoDeclarationAction.findTargetElement(getProject(), getEditor(), getEditor().getCaretModel().getOffset());
        assertThat(element.getContainingFile(), is(files[1]));
        get
        getEditor().getCaretModel().moveToOffset(element.getTextOffset());
        getEditor().getScrollingModel().scrollToCaret(ScrollType.CENTER);
        getEditor().getSelectionModel().removeSelection();*/

        myFixture.checkResultByFile("Test2.expected.elm");
    }
}
