package mkolaczek.elm.features.documentation;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;
import mkolaczek.elm.features.ElmDocumentationProvider;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class DocumentationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }

    public void testModuleQuickNavigateInfo() {
        myFixture.configureByFile("Test.elm");
        Module module = ((ElmFile) myFixture.getFile()).module();
        String info = new ElmDocumentationProvider().getQuickNavigateInfo(module, null);
        String expected = "effect module Test1.B\n" +
                "&nbsp&nbsp&nbsp&nbsp where { a = A }\n" +
                "&nbsp&nbsp&nbsp&nbsp exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, ( ** ), (<:), A( .. ), B(Abc, Def, Ghci),\n" +
                "&nbsp&nbsp&nbsp&nbsp C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))";
        assertThat(info, is(wrapinHtml(expected)));
    }

    public void testModuleQuickDocumentation() {
        myFixture.configureByFile("Test.elm");
        Module module = ((ElmFile) myFixture.getFile()).module();
        String doc = new ElmDocumentationProvider().generateDoc(module, null);
        String expected = wrapinHtml("<pre>effect module Test1.B\n" +
                "&nbsp&nbsp&nbsp&nbsp where { a = A }\n" +
                "&nbsp&nbsp&nbsp&nbsp exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, ( ** ), (<:), A( .. ), B(Abc, Def, Ghci),\n" +
                "&nbsp&nbsp&nbsp&nbsp C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))</pre>" +
                "<p>{-|\n" +
                "{- -}\n\n" +
                "sdsd\n" +
                "-}</p>");
        assertThat(doc, is(expected));
    }

    @NotNull
    private String wrapinHtml(String text) {
        return "<html><head></head><body>" +
                text +
                "</body></html>";
    }

    public void testTypeQuickNavigateInfo() {
        myFixture.configureByFile("Test.elm");
        TypeDeclaration type = ((ElmFile) myFixture.getFile()).module()
                                                              .typeDeclaration("B")
                                                              .get();
        String info = new ElmDocumentationProvider().getQuickNavigateInfo(type, null);
        String expected = "type B = Abc | Def | Ghci";
        assertThat(info, is(wrapinHtml(expected)));
    }

    public void testTypeQuickDocumentation() {
        myFixture.configureByFile("Test.elm");
        TypeDeclaration type = ((ElmFile) myFixture.getFile()).module()
                                                              .typeDeclaration("B")
                                                              .get();
        String info = new ElmDocumentationProvider().generateDoc(type, null);
        String expected = wrapinHtml(
                "<pre>type B = Abc | Def | Ghci</pre>" +
                        "<p>{-| Test type -}</p>"
        );
        assertThat(info, is(expected));
    }

    public void testTypeConstructorQuickNavigateInfo() {
        myFixture.configureByFile("Test.elm");
        TypeConstructor type = ((ElmFile) myFixture.getFile()).module()
                                                              .typeDeclaration("B")
                                                              .get()
                                                              .constructor("Ghci")
                                                              .get();
        String info = new ElmDocumentationProvider().getQuickNavigateInfo(type, null);
        String expected = wrapinHtml("type B = Abc | Def | Ghci");
        assertThat(info, is(expected));
    }

    public void testTypeConstructorDocumentation() {
        myFixture.configureByFile("Test.elm");
        TypeConstructor type = ((ElmFile) myFixture.getFile()).module()
                                                              .typeDeclaration("B")
                                                              .get()
                                                              .constructor("Ghci")
                                                              .get();
        String info = new ElmDocumentationProvider().generateDoc(type, null);
        String expected = wrapinHtml(
                "<pre>type B = Abc | Def | Ghci</pre>" +
                        "<p>{-| Test type -}</p>"
        );
        assertThat(info, is(expected));
    }

}
