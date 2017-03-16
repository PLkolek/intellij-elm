package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SymbolAutocompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return "testdata/autocompletion/symbol";
    }

    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }

    public void testSymbolDeclarationCompletion() {
        myFixture.configureByFiles("declaration/Test.elm");
        autocomplete("***");
    }

    public void testSymbolCompletionInExposing() {
        myFixture.configureByFiles("exposing/Test.elm");
        autocomplete("***");
    }
}