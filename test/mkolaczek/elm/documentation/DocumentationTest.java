package mkolaczek.elm.documentation;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ElmModule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DocumentationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/documentation";
    }

    public void testModuleQuickNavigateInfo() {
        myFixture.configureByFile("Test.elm");
        ElmModule module = ((ElmFile) myFixture.getFile()).module();
        String info = new ElmDocumentationProvider().getQuickNavigateInfo(module, null);
        String expected = "<html><head></head><body>" +
                "module Test1.B\n" +
                "&nbsp&nbsp&nbsp&nbsp exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, ( ** ), (,,), A( .. ), B(Abc, Def, Ghci),\n" +
                "&nbsp&nbsp&nbsp&nbsp C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))" +
                "</body></html>";
        assertThat(info, is(expected));
    }

    public void testModuleQuickDocumentation() {
        myFixture.configureByFile("Test.elm");
        ElmModule module = ((ElmFile) myFixture.getFile()).module();
        String doc = new ElmDocumentationProvider().generateDoc(module, null);
        String expected = "<html><head></head><body>" +
                "<pre>module Test1.B\n" +
                "&nbsp&nbsp&nbsp&nbsp exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, ( ** ), (,,), A( .. ), B(Abc, Def, Ghci),\n" +
                "&nbsp&nbsp&nbsp&nbsp C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))</pre>" +
                "<p>{-|\n" +
                "{- -}\n\n" +
                "sdsd\n" +
                "-}</p>" +
                "</body></html>";
        assertThat(doc, is(expected));
    }


}
