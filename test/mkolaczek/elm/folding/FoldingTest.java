package mkolaczek.elm.folding;

import com.intellij.codeInsight.folding.impl.CodeFoldingManagerImpl;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.ex.FoldingModelEx;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FoldingTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/folding";
    }

    public void testModuleQuickNavigateInfo() {
        myFixture.configureByFile("Test.elm");
        CodeFoldingManagerImpl.getInstance(getProject()).buildInitialFoldings(myFixture.getEditor());
        FoldingModelEx foldingModel = (FoldingModelEx) myFixture.getEditor().getFoldingModel();
        foldingModel.rebuild();
        myFixture.doHighlighting();
        String text = getEditor().getDocument().getText();

        assertNull(regionAt(foldingModel, text, "{-"));
        assertThat(regionAt(foldingModel, text, "aaaaaaa").getPlaceholderText(), is(("exposing ...")));
        assertThat(regionAt(foldingModel, text, "Test2").getPlaceholderText(), is(("import ...")));
    }

    private FoldRegion regionAt(FoldingModelEx foldingModel, String text, String substr) {
        return foldingModel.getCollapsedRegionAtOffset(text.indexOf(substr));
    }
}

