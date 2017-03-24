package mkolaczek.elm.features.autocompletion;


import com.intellij.codeInsight.completion.LightFixtureCompletionTestCase;
import com.intellij.codeInsight.lookup.LookupElement;
import mkolaczek.elm.TestUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KeywordCompletionTest extends LightFixtureCompletionTestCase {


    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass(), "keyword");
    }

    public void testAfterImportsCompletion() {
        performTest("import/Test.elm", "import/expected.elm",
                "import", "type", "infixr", "infixl", "infix", "port");
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

    public void testModuleExposingExistingParenthesesCompletion() {
        performTest("moduleExposingExistingParentheses/Test.elm",
                "moduleExposingExistingParentheses/expected.elm",
                "exposing");
    }

    public void testEffectModuleWhereCompletion() {
        performTest("effectModuleWhere/Test.elm", "effectModuleWhere/expected.elm", "where");
    }

    public void testModuleCompletion() {
        performTest("module/Test.elm", "module/expected.elm", "effect module", "module", "port module");
    }

    public void testAliasCompletion() {
        performTest("alias/Test.elm", "alias/expected.elm", "alias");
    }

    public void testPortCompletion() {
        performTest("port/Test.elm", "port/expected.elm", "port", "type", "infixr", "infixl", "infix", "import", "B");
    }

    public void testExpressionInIfConditionCompletion() {
        performTest("expression/inIf/Condition.elm",
                "expression/inIf/Condition.expected.elm",
                "let", "if", "then", "else", "case", "someValue");
    }

    public void testExpressionInLastBinaryOperationCompletion() {
        performTest("expression/inBinaryOperation/Last.elm",
                "expression/inBinaryOperation/Last.expected.elm",
                "let", "if", "case", "someValue");
    }

    public void testInCompletion() {
        performTest("expression/inLet/In.elm", "expression/inLet/In.expected.elm", "in", "someVal", "letVal");
    }

    public void testOfCompletion() {
        performTest("expression/inCase/Of.elm", "expression/inCase/Of.expected.elm", "of", "someValue");
    }

    private void performTest(String beforeFile, String afterFile, String... suggestions) {
        configureByFile(beforeFile);
        List<String> strings = autocomplete();
        assertThat(strings, hasItems(suggestions));
        assertThat(strings.size(), is(suggestions.length));
        if (suggestions.length > 0) {
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            LookupElement item = Arrays.stream(myItems)
                                       .filter(i -> i.getLookupString().equals(suggestions[0]))
                                       .findFirst()
                                       .get();
            selectItem(item);
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