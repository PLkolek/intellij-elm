package mkolaczek.elm.features.refactoring.rename;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import mkolaczek.elm.boilerplate.ElmLanguage;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import mkolaczek.elm.psi.node.ValueName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InPlaceRenamerTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private
    Editor editor;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private
    Module module;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private
    TypeDeclaration type;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private
    TypeConstructor constructor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private
    ValueName valueName;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private
    PsiElement something;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isIdentifier() throws Exception {
        assertFalse(renamer(module).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(renamer(module).isIdentifier("A.B", ElmLanguage.INSTANCE));

        assertFalse(renamer(type).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(renamer(type).isIdentifier("A", ElmLanguage.INSTANCE));
        assertFalse(renamer(type).isIdentifier("A.B", ElmLanguage.INSTANCE));

        assertFalse(renamer(constructor).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(renamer(constructor).isIdentifier("A", ElmLanguage.INSTANCE));
        assertFalse(renamer(constructor).isIdentifier("A.B", ElmLanguage.INSTANCE));

        assertFalse(renamer(valueName).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(renamer(valueName).isIdentifier("a", ElmLanguage.INSTANCE));
        assertFalse(renamer(valueName).isIdentifier("A", ElmLanguage.INSTANCE));
        assertFalse(renamer(valueName).isIdentifier("a.b", ElmLanguage.INSTANCE));
    }

    @NotNull
    private InplaceRenamer renamer(PsiNamedElement element) {
        return new InplaceRenamer(element, something, editor) {
            @Override
            protected boolean notSameFile(@Nullable VirtualFile file, @NotNull PsiFile containingFile) {
                return false;
            }

            @Override
            protected void showDialogAdvertisement(String actionId) {
            }
        };
    }

}