package mkolaczek.elm.safeDelete;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.BaseRefactoringProcessor;
import com.intellij.refactoring.MultiFileTestCase;
import com.intellij.refactoring.safeDelete.SafeDeleteHandler;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInsight.TargetElementUtil.ELEMENT_NAME_ACCEPTED;
import static com.intellij.codeInsight.TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SafeDeleteTest extends MultiFileTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/safeDelete/";
    }


    @NotNull
    @Override
    protected String getTestRoot() {
        return "";
    }

    public void testSafeDeleteUsedModule() throws Exception {
        configureByFiles(null, "used/Test1.elm", "used/Test2.elm");
        try {
            safeDelete();
        } catch (BaseRefactoringProcessor.ConflictsInTestsException e) {
            assertThat(e.getMessage(),
                    is("module <b><code>Test2</code></b> has 2 usages that are not safe to delete.<br>Of those 1 usage is in strings, comments, non-code files or generated code."));
        }
        assertNotNull(getProject().getBaseDir().getChildren()[1].findChild("Test2.elm"));
    }

    public void testSafeDeleteModuleUsedOnlyByItself() throws Exception {
        configureByFiles(null, "usedByItselfOnly/Test2.elm");
        safeDelete();
        assertNull(getProject().getBaseDir().getChildren()[1].findChild("Test2.elm"));
    }

    private void safeDelete() {
        final PsiElement psiElement = TargetElementUtil.findTargetElement(myEditor, ELEMENT_NAME_ACCEPTED | REFERENCED_ELEMENT_ACCEPTED);
        assertNotNull("No element found in text:\n" + getFile().getText(), psiElement);
        SafeDeleteHandler.invoke(getProject(), new PsiElement[]{psiElement}, true);
    }

}
