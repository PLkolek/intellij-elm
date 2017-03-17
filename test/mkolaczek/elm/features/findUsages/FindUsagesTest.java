package mkolaczek.elm.features.findUsages;

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
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages("module/Test1.elm", "module/Test2.elm");
        assertThat(usageInfos.size(), is(1));
    }

    public void testFindTypeUsages() {
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages("type/Test.elm");
        assertThat(usageInfos.size(), is(2));
    }

    public void testFindConstructorUsages() {
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages("typeConstructor/Test.elm");
        assertThat(usageInfos.size(), is(1));
    }

}
