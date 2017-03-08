package mkolaczek.elm.features.inspections.unusedDeclaration;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.roots.CollectingContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.features.inspections.UnusedDeclarationInspection;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class UnusedDeclarationTest extends LightCodeInsightFixtureTestCase {

    private final CollectingContentIterator collector = new CollectingContentIterator() {
        final List<VirtualFile> files = Lists.newArrayList();

        @NotNull
        @Override
        public List<VirtualFile> getFiles() {
            return files;
        }


        @Override
        public boolean processFile(VirtualFile fileOrDir) {
            if (fileOrDir.getName().equals("Test.elm")) {
                files.add(fileOrDir);
            }
            return true;
        }
    };

    @Override
    protected String getTestDataPath() {
        return "testdata/inspections/unusedDeclaration";
    }

    public void testUnusedModule() {
        myFixture.configureByFiles("Test.elm");
        myFixture.enableInspections(Collections.singleton(UnusedDeclarationInspection.class));
        myFixture.checkHighlighting();
        final IntentionAction intention = myFixture.findSingleIntention("Remove");
        assertNotNull(intention);

        myFixture.launchAction(intention);
        ProjectFileIndex.SERVICE.getInstance(getProject()).iterateContent(collector);

        assertEmpty(collector.getFiles());
    }

    public void testInspectionDoesntFailOnIncompleteFile() {
        myFixture.configureByFiles("Empty.elm");
        myFixture.enableInspections(Collections.singleton(UnusedDeclarationInspection.class));
        myFixture.checkHighlighting();
        myFixture.configureByFiles("Incomplete.elm");
        myFixture.enableInspections(Collections.singleton(UnusedDeclarationInspection.class));
        myFixture.checkHighlighting();
    }

}
