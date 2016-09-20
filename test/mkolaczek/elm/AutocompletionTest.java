package mkolaczek.elm;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AutocompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return "testdata";
    }

    public void testCompletion() {
        myFixture.configureByFiles("Test2.elm", "Test1.elm");
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertTrue(strings.contains("Test1"));
        assertThat(strings.size(), is(2)); //TODO: contains current module too
    }

}
