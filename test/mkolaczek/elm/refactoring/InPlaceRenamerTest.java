package mkolaczek.elm.refactoring;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import mkolaczek.elm.ElmLanguage;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmTypeConstructor;
import mkolaczek.elm.psi.node.ElmTypeName;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InPlaceRenamerTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Editor editor;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ElmModule module;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ElmTypeName type;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ElmTypeConstructor constructor;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    PsiElement something;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isIdentifier() throws Exception {
        assertFalse(new InplaceRenamer(module, something, editor).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(new InplaceRenamer(module, something, editor).isIdentifier("A.B", ElmLanguage.INSTANCE));

        assertFalse(new InplaceRenamer(type, something, editor).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(new InplaceRenamer(type, something, editor).isIdentifier("A", ElmLanguage.INSTANCE));
        assertFalse(new InplaceRenamer(type, something, editor).isIdentifier("A.B", ElmLanguage.INSTANCE));

        assertFalse(new InplaceRenamer(constructor, something, editor).isIdentifier("123", ElmLanguage.INSTANCE));
        assertTrue(new InplaceRenamer(constructor, something, editor).isIdentifier("A", ElmLanguage.INSTANCE));
        assertFalse(new InplaceRenamer(constructor, something, editor).isIdentifier("A.B", ElmLanguage.INSTANCE));
    }

}