package mkolaczek.elm.unresolvedReference;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class UnresolvedReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/unresolvedReference/";
    }

    public void testUnresolved() {
        myFixture.configureByFiles("Test.elm");
        myFixture.checkHighlighting();
    }

}
