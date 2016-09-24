package mkolaczek.elm.rename;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class RenameTest extends LightCodeInsightFixtureTestCase {
    @Override
    protected String getTestDataPath() {
        return "testdata/rename";
    }

    public void testFindModuleUsages() {
        myFixture.configureByFiles("Test1.elm", "Test2.elm");
        myFixture.renameElementAtCaret("NewName");
        myFixture.checkResultByFile("Test1.elm", "Test1.expected.elm", false);
        myFixture.checkResultByFile("Test2.elm", "Test2.expected.elm", false);
    }

}
