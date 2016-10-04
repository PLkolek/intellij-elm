package mkolaczek.elm.formatting;

import com.intellij.formatting.DependantSpacingImpl;
import com.intellij.formatting.DependentSpacingRule;
import com.intellij.formatting.engine.BlockRangesMap;
import com.intellij.openapi.util.TextRange;


public class ElmAfterLParenDependantSpacing extends DependantSpacingImpl {

    private static final int DEPENDENT_REGION_LF_CHANGED_MASK = 0x20;

    private final TextRange dependency;

    boolean parentHasLF = false;

    public ElmAfterLParenDependantSpacing(TextRange dependency) {
        super(0, 1, dependency, false, 0, DependentSpacingRule.DEFAULT);
        this.dependency = dependency;
    }

    @Override
    public int getMinLineFeeds() {
        return 0;
    }

    @Override
    public int getKeepBlankLines() {
        return 0;
    }

    @Override
    public int getMinSpaces() {
        return parentHasLF ? 1 : 0;
    }

    @Override
    public int getMaxSpaces() {
        return getMinSpaces();
    }

    @Override
    public void refresh(BlockRangesMap helper) {
        parentHasLF = (myFlags & DEPENDENT_REGION_LF_CHANGED_MASK) != 0 && helper.containsLineFeeds(dependency);
    }


}
