package mkolaczek.elm.autocompletion;


import com.intellij.codeInsight.completion.LightFixtureCompletionTestCase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KeywordAutocompletionTest extends LightFixtureCompletionTestCase {


    @Override
    protected String getTestDataPath() {
        return "testdata/autocompletion/keyword";
    }

    public void testImportCompletion() {
        performTest("import/Test.elm", "import/expected.elm", "import");
    }

    public void testImportCompletionDoesntTriggerInTheMiddleOfAnImport() {
        performTest("import/AutocompleteInTheMiddleOfAnImport.elm",
                "import/expectedAutocompleteInTheMiddleOfAnImport.elm"
        );
    }

    public void testAfterImportedModuleNameCompletion() {
        performTest("afterImportedModuleName/Test.elm", "afterImportedModuleName/expected.elm", "as", "exposing");
    }

    public void testImportExposingCompletion() {
        performTest("importExposing/Test.elm", "importExposing/expected.elm", "exposing");
    }

    public void testModuleExposingCompletion() {
        performTest("moduleExposing/Test.elm", "moduleExposing/expected.elm", "exposing");
    }

    public void testEffectModuleWhereCompletion() {
        performTest("effectModuleWhere/Test.elm", "effectModuleWhere/expected.elm", "where");
    }

    public void testModuleCompletion() {
        performTest("module/Test.elm", "module/expected.elm", "module", "effect module", "port module");
    }

    private void performTest(String beforeFile, String afterFile, String... suggestions) {
        configureByFile(beforeFile);
        List<String> strings = autocomplete();
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
        if (suggestions.length > 0) {
            selectItem(myItems[0]);
        }
        checkResultByFile(afterFile);
    }

    @NotNull
    private List<String> autocomplete() {
        complete();
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        return strings;
    }
}