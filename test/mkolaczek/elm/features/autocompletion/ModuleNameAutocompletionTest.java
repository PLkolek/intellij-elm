package mkolaczek.elm.features.autocompletion;


import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ModuleNameAutocompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return "testdata/autocompletion/moduleName";
    }

    public void testImportCompletion() {
        myFixture.configureByFiles("import/Test2.elm", "Test1.elm");
        autocomplete("Test1");
    }

    public void testModuleNameCompletion() {
        myFixture.configureByFiles("moduleName/Test2.elm", "Test1.elm");
        autocomplete("Test2");

    }

    public void testAsAutocompletion() {
        myFixture.configureByFiles("as/Test2.elm", "Test1.elm");
        autocomplete("A", "B", "C", "BC");
    }

    public void testInQualifiedTypeAutocompletion() {
        myFixture.configureByFiles("inQualifiedType/matches/Test.elm", "inQualifiedType/matches/PrefixSuffix.elm");
        autocomplete("Suffix");
    }

    public void testInQualifiedTypeDoesntMatchAutocompletion() {
        myFixture.configureByFiles("inQualifiedType/doesntMatch/Test.elm",
                "inQualifiedType/doesntMatch/PrefixSuffix.elm");
        autocomplete();
    }


    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }
}