package mkolaczek.elm.inspections.unusedDeclaration;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.inspections.UnusedDeclarationInspection;

public class UnusedDeclarationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/inspections/unusedDeclaration";
    }

    public void testUnusedModule() {
        myFixture.configureByFiles("Test.elm");
        myFixture.enableInspections(UnusedDeclarationInspection.class);
        myFixture.checkHighlighting();
    }

}
