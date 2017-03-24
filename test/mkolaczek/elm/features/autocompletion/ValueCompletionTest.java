package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValueCompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass(), "value");
    }

    public void testSimpleValueDeclarationCompletion() {
        myFixture.configureByFiles("declaration/Simple.elm");
        autocomplete("someValue", "otherValue", "port", "infix", "infixl", "infixr", "type", "(***)");
    }

    public void testValueDeclarationInRecordCompletion() {
        myFixture.configureByFiles("declaration/InRecord.elm");
        autocomplete("someValue", "otherValue");
    }

    public void testValueDeclarationInListCompletion() {
        myFixture.configureByFiles("declaration/InList.elm");
        autocomplete("someValue", "otherValue");
    }


    public void testExposingValueFromPatternCompletion() {
        myFixture.configureByFiles("exposing/FromPattern.elm");
        autocomplete("val1", "val2", "val3");
    }

    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }
}