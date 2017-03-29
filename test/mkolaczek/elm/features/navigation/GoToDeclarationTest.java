package mkolaczek.elm.features.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

import java.util.Arrays;

public class GoToDeclarationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }

    public void testModuleDeclaration() {
        doTest("module", "Test1.elm", "Test2.elm");
    }

    public void testOperatorDeclaration() {
        doTest("operator", "Test1.elm", "Test2.elm");
    }

    public void testPortDeclaration() {
        doTest("port", "Test2.elm");
    }

    public void testValueDeclaration() {
        doTest("value", "Test1.elm", "Test2.elm");
    }

    public void testBuiltInValueDeclaration() {
        doTest("value/builtInImport", "UsingBuiltIn.elm", "List.elm");
    }

    private void doTest(String dir, String... fileNames) {
        String[] fullFileNames = Arrays.stream(fileNames).map(n -> dir + "/" + n).toArray(String[]::new);
        PsiFile[] files = myFixture.configureByFiles(fullFileNames);
        //com.intellij.openapi.fileEditor.impl.TestEditorManagerImpl.openEditor() returns empty list,
        //which prevents GoToDeclarationAction from opening new editor, so here I do it manually
        Editor oldEditor = getEditor();
        myFixture.openFileInEditor(files[files.length - 1].getVirtualFile());
        new GotoDeclarationAction().invoke(getProject(), oldEditor, files[0]);

        String lastFile = fileNames[fileNames.length - 1];
        String withoutExtension = lastFile.substring(0, lastFile.indexOf("."));
        String expected = "/" + withoutExtension + ".expected.elm";

        myFixture.checkResultByFile(dir + expected);
    }
}
