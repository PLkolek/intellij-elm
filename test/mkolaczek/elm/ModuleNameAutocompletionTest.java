package mkolaczek.elm;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
        List<String> strings = autocomplete();
        assertTrue(strings.contains("Test1"));
        assertThat(strings.size(), is(1)); //TODO: contains current module too
    }

    public void testModuleNameCompletion() {
        myFixture.configureByFiles("moduleName/Test2.elm", "Test1.elm");
        List<String> strings = autocomplete();
        assertTrue(strings.contains("Test2"));
        assertThat(strings.size(), is(1));
    }

    @NotNull
    private List<String> autocomplete() {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        return strings;
    }

    public void testAsAutocompletion() {
        myFixture.configureByFiles("as/Test2.elm", "Test1.elm");
        List<String> strings = autocomplete();
        assertTrue(strings.containsAll(Arrays.asList("A", "B", "C", "BC")));
        assertThat(strings.size(), is(4));
    }
}
