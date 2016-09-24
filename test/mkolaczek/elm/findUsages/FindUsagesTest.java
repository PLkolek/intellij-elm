package mkolaczek.elm.findUsages;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.usageView.UsageInfo;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FindUsagesTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testdata/findUsages";
    }

    public void testFindModuleUsages() {
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages("Test1.elm", "Test2.elm");
        assertThat(usageInfos.size(), is(1));
    }

}
