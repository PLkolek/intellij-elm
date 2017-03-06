package mkolaczek.elm.autocompletion;


import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TypeAutocompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return "testdata/autocompletion/type";
    }

    public void testTypeNameCompletion() {
        myFixture.configureByFiles("typeName/Test.elm");
        autocomplete("Type1", "Type2", "alias");
    }

    public void testAliasNameCompletion() {
        myFixture.configureByFiles("aliasName/Test.elm");
        myFixture.complete(CompletionType.BASIC, 1);
        assertNull(myFixture.getLookupElementStrings());
        myFixture.checkResultByFile("aliasName/Expected.elm");
    }

    public void testInTypeExpressionCompletion() {
        myFixture.configureByFiles("typeExpression/Test.elm");
        autocomplete("SomeType", "Alias");
    }

    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }
}