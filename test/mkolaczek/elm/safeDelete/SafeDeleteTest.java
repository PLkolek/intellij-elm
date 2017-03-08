package mkolaczek.elm.safeDelete;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.BaseRefactoringProcessor;
import com.intellij.refactoring.MultiFileTestCase;
import com.intellij.refactoring.safeDelete.SafeDeleteHandler;
import com.intellij.testFramework.PlatformTestUtil;
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
        configureByFiles(null, "module/used/Test1.elm", "module/used/Test2.elm");
        try {
            safeDelete();
        } catch (BaseRefactoringProcessor.ConflictsInTestsException e) {
            assertThat(e.getMessage(),
                    is("module <b><code>Test2</code></b> has 2 usages that are not safe to delete.<br>Of those 1 usage is in strings, comments, non-code files or generated code."));
        }
        assertNotNull(getProject().getBaseDir().getChildren()[1].findChild("Test2.elm"));
    }

    public void testSafeDeleteModuleUsedOnlyByItself() throws Exception {
        configureByFiles(null, "module/usedByItselfOnly/Test2.elm");
        safeDelete();
        assertNull(getProject().getBaseDir().getChildren()[1].findChild("Test2.elm"));
    }

    public void testSafeDeleteUsedType() throws Exception {
        configureByFiles(null, "type/used/Test.elm");
        try {
            safeDelete();
        } catch (BaseRefactoringProcessor.ConflictsInTestsException e) {
            assertThat(e.getMessage(),
                    is("type <b><code>Type</code></b> has 2 usages that are not safe to delete.<br>Of those 1 usage is in strings, comments, non-code files or generated code."));
        }
    }

    public void testSafeDeleteTypeUsedOnlyByItself() throws Exception {
        configureByFiles(null, "type/usedByItselfOnly/Test.elm");
        safeDelete();
        VirtualFile fileByPath = LocalFileSystem.getInstance()
                                                .findFileByPath(getTestDataPath() + "type/usedByItselfOnly/Expected.elm");
        //noinspection ConstantConditions
        PlatformTestUtil.assertFilesEqual(myFile.getVirtualFile(), fileByPath);
    }


    private void safeDelete() {
        final PsiElement psiElement = TargetElementUtil.findTargetElement(myEditor,
                ELEMENT_NAME_ACCEPTED | REFERENCED_ELEMENT_ACCEPTED);
        assertNotNull("No element found in text:\n" + getFile().getText(), psiElement);
        SafeDeleteHandler.invoke(getProject(), new PsiElement[]{psiElement}, true);
    }

}
