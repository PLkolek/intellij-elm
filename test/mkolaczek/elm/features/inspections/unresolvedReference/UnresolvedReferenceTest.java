package mkolaczek.elm.features.inspections.unresolvedReference;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class UnresolvedReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/inspections/unresolvedReference/";
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
