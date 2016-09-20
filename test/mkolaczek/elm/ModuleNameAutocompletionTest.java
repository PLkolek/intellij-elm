package mkolaczek.elm;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ModuleNameAutocompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return "testdata/autocompletion/moduleName";
    }

    public void testImportCompletion() {
        myFixture.configureByFiles("import/Test2.elm", "Test1.elm");
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertTrue(strings.contains("Test1"));
        assertThat(strings.size(), is(1)); //TODO: contains current module too
    }

    public void testModuleNameCompletion() {
        myFixture.configureByFiles("moduleName/Test2.elm", "Test1.elm");
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertTrue(strings.contains("Test2"));
        assertThat(strings.size(), is(1));
    }

}
