package mkolaczek.elm.features.safeDelete;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.BaseRefactoringProcessor;
import com.intellij.refactoring.MultiFileTestCase;
import com.intellij.refactoring.safeDelete.SafeDeleteHandler;
import com.intellij.testFramework.PlatformTestUtil;
import mkolaczek.elm.TestUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.intellij.codeInsight.TargetElementUtil.ELEMENT_NAME_ACCEPTED;
import static com.intellij.codeInsight.TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SafeDeleteTest extends MultiFileTestCase {

    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }


    @NotNull
    @Override
    protected String getTestRoot() {
        return "";
    }

    public void testSafeDeleteUsedModule() throws Exception {
        deleteUsed(
                "module <b><code>Test2</code></b> has 2 usages that are not safe to delete.<br>Of those 1 usage is in strings, comments, non-code files or generated code.",
                "module/used/Test1.elm",
                "module/used/Test2.elm");
        assertNotNull(getProject().getBaseDir().getChildren()[1].findChild("Test2.elm"));
    }

    public void testSafeDeleteModuleUsedOnlyByItself() throws Exception {
        configureByFiles(null, "module/usedByItselfOnly/Test2.elm");
        safeDelete();
        assertNull(getProject().getBaseDir().getChildren()[1].findChild("Test2.elm"));
    }

    public void testSafeDeleteUsedType() throws Exception {
        deleteUsed(
                "type <b><code>Type</code></b> has 2 usages that are not safe to delete.<br>Of those 1 usage is in strings, comments, non-code files or generated code.",
                "type/used/Test.elm");
    }

    public void testSafeDeleteTypeUsedOnlyByItself() throws Exception {
        deleteUnused("type/usedByItselfOnly/Test.elm", "type/usedByItselfOnly/Expected.elm");
    }

    public void testSafeDeleteUsedTypeConstructor() throws Exception {
        deleteUsed(
                "type constructor <b><code>Constructor</code></b> has 1 usage that is not safe to delete.<br>Of those 1 usage is in strings, comments, non-code files or generated code.",
                "typeConstructor/used/Test.elm");
    }

    public void testSafeDeleteUnusedTypeConstructor() throws Exception {
        deleteUnused("typeConstructor/notUsed/Test.elm", "typeConstructor/notUsed/Expected.elm");
    }

    private void deleteUnused(String file, String expected) throws IOException {
        configureByFiles(null, file);
        safeDelete();
        VirtualFile fileByPath = LocalFileSystem.getInstance()
                                                .findFileByPath(getTestDataPath() + expected);
        //noinspection ConstantConditions
        PlatformTestUtil.assertFilesEqual(myFile.getVirtualFile(), fileByPath);
    }

    private void deleteUsed(String error, String... files) {
        configureByFiles(null, files);
        try {
            safeDelete();
        } catch (BaseRefactoringProcessor.ConflictsInTestsException e) {
            assertThat(e.getMessage(),
                    is(error));
        }
    }


    private void safeDelete() {
        PsiElement psiElement = TargetElementUtil.findTargetElement(myEditor,
                ELEMENT_NAME_ACCEPTED | REFERENCED_ELEMENT_ACCEPTED);
        assertNotNull("No element found in text:\n" + getFile().getText(), psiElement);
        SafeDeleteHandler.invoke(getProject(), new PsiElement[]{psiElement}, true);
    }

}
