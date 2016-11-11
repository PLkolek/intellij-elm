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
        String expected = "module Test1.B exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, (    **              )," +
                " (,,), A(           ..     ), B(Abc, Def, Ghci), C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))";
        assertThat(info, is(expected));

    }
}
