package mkolaczek.elm.features.refactoring.rename;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

public class RenameTest extends LightCodeInsightFixtureTestCase {
    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }

    public void testRenameModule() {
        myFixture.configureByFiles("module/Test1.elm", "module/Test2.elm");
        myFixture.renameElementAtCaret("NewName");
        myFixture.checkResultByFile("module/Test1.elm", "module/Test1.expected.elm", false);
        myFixture.checkResultByFile("module/Test2.elm", "module/Test2.expected.elm", false);
    }

    public void testRenameType() {
        myFixture.configureByFiles("type/Test.elm");
        myFixture.renameElementAtCaret("NewName");
        myFixture.checkResultByFile("type/Test.elm", "type/Test.expected.elm", false);
    }

    public void testRenameTypeConstructor() {
        myFixture.configureByFiles("typeConstructor/Test.elm");
        myFixture.renameElementAtCaret("NewName");
        myFixture.checkResultByFile("typeConstructor/Test.elm", "typeConstructor/Test.expected.elm", false);
    }

    public void testRenameOperator() {
        myFixture.configureByFiles("operator/Test.elm");
        myFixture.renameElementAtCaret("<->");
        myFixture.checkResultByFile("operator/Test.elm", "operator/Test.expected.elm", false);
    }

    public void testRenamePort() {
        myFixture.configureByFiles("port/Test.elm");
        myFixture.renameElementAtCaret("newName");
        myFixture.checkResultByFile("port/Test.elm", "port/Test.expected.elm", false);
    }

    public void testRenameValue() {
        myFixture.configureByFiles("value/Test.elm");
        myFixture.renameElementAtCaret("newName");
        myFixture.checkResultByFile("value/Test.elm", "value/Test.expected.elm", false);
    }

}
