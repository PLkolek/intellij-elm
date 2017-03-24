package mkolaczek.elm.features.autocompletion;


import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TypeConstructorCompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass(), "typeConstructor");
    }

    public void testTypeConstructorNameCompletion() {
        myFixture.configureByFiles("typeConstructorName/Test.elm");
        autocomplete("ACons", "BCons", "ACons | BCons");
    }

    public void testInExposingCompletion() {
        myFixture.configureByFiles("exposing/Test.elm");
        autocomplete("BCons");
    }

    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }
}