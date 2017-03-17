package mkolaczek.elm.features.inspections.unresolvedReference;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;

public class UnresolvedReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }

    public void testUnresolved() {
        myFixture.configureByFiles("Test.elm", "ModuleToImport.elm");
        myFixture.checkHighlighting();
        IntentionAction intention = myFixture.findSingleIntention("Import module");
        assertNotNull(intention);

        myFixture.launchAction(intention);
        myFixture.checkResultByFile("Expected.elm");
    }

}
