package mkolaczek.elm.features.refactoring.rename;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class RenameTest extends LightCodeInsightFixtureTestCase {
    @Override
    protected String getTestDataPath() {
        return "testdata/rename";
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

}
