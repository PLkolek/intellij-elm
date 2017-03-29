package mkolaczek.elm.features.autocompletion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

import java.util.List;

import static mkolaczek.elm.TestUtil.files;
import static mkolaczek.elm.TestUtil.withBuiltIn;
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
        autocomplete(withBuiltIn("someValue", "otherValue", "port", "infix", "infixl", "infixr", "type", "(***)"));
    }

    public void testValueDeclarationInRecordCompletion() {
        myFixture.configureByFiles("declaration/InRecord.elm");
        autocomplete(withBuiltIn("someValue", "otherValue"));
    }

    public void testValueDeclarationInListCompletion() {
        myFixture.configureByFiles("declaration/InList.elm");
        autocomplete(withBuiltIn("someValue", "otherValue"));
    }


    public void testExposingValueFromPatternCompletion() {
        myFixture.configureByFiles("exposing/FromPattern.elm");
        autocomplete("val1", "val2", "val3");
    }

    public void testValueInExpressionCompletion() {
        myFixture.configureByFiles(files("inExpression/", "Importing.elm", "Imported.elm"));
        autocomplete("somePort", "Cons1", "Cons2");
    }

    public void testValueInLetExpressionCompletion() {
        myFixture.configureByFiles(files("inLetExpression/", "Let.elm"));
        autocomplete("lTopLevel", "lMiddleLevel", "lBottomLevel", "let");
    }

    public void testValueInCaseExpressionCompletion() {
        myFixture.configureByFiles(files("inCaseExpression/", "Case.elm"));
        autocomplete("cTopLevel", "cMidLevel", "cBottomLevel", "case");
    }

    public void testValueInLambdaExpressionCompletion() {
        myFixture.configureByFiles(files("inLambdaExpression/", "Lambda.elm"));
        autocomplete(withBuiltIn("lambda", "arg1", "tup1", "tup2", "let", "case", "if"));
    }

    public void testValueInFunctionDefinitionCompletion() {
        myFixture.configureByFiles(files("inFunctionDefinition/", "Function.elm"));
        autocomplete("arg1", "arg2", "case");
    }

    private void autocomplete(String... suggestions) {
        myFixture.complete(CompletionType.BASIC, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertNotNull(strings);
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
    }
}