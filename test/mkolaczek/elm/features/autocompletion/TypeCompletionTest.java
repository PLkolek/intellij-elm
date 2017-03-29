package mkolaczek.elm.features.autocompletion;


import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

import java.util.List;

import static mkolaczek.elm.TestUtil.withBuiltIn;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TypeCompletionTest extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass(), "type");
    }

    public void testTypeNameCompletion() {
        myFixture.configureByFiles("typeName/Test.elm");
        autocomplete("Type1", "Type2", "alias");
    }

    public void testQualifiedTypeNameCompletion() {
        myFixture.configureByFiles("qualifiedTypeName/Test.elm", "qualifiedTypeName/Test2.elm");
        autocomplete("SomeType");
    }

    public void testAliasNameCompletion() {
        myFixture.configureByFiles("aliasName/Test.elm");
        myFixture.complete(CompletionType.BASIC, 1);
        assertNull(myFixture.getLookupElementStrings());
        myFixture.checkResultByFile("aliasName/Expected.elm");
    }

    public void testInTypeExpressionCompletion() {
        myFixture.configureByFiles("typeExpression/Test.elm");
        autocomplete(withBuiltIn("SomeType", "List", "String", "Int", "Float", "Bool", "Char"));
    }

    public void testInExposingCompletion() {
        myFixture.configureByFiles("exposing/Test.elm");
        autocomplete("BType");
    }

    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }
}