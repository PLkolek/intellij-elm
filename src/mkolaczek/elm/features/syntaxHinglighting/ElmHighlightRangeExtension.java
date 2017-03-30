package mkolaczek.elm.features.syntaxHinglighting;

import com.intellij.codeInsight.daemon.impl.HighlightRangeExtension;
import com.intellij.psi.PsiFile;
import mkolaczek.elm.boilerplate.ElmFileType;
import org.jetbrains.annotations.NotNull;


/**
 * WTF
 * <p>
 * Annotators start at the leaves, and if any of them reports any error, the ancestors of this elements are not processed.
 * This extensions forces annotating up the tree.
 * <p>
 * Why? For syntax highlighting either:
 * a) we process only the Declarations node and its direct children to highlight top-level values
 * b) for every value we check if it's top level
 * <p>
 * When choosing a), which seems nicer, we must force highlighting of parents.
 */
public class ElmHighlightRangeExtension implements HighlightRangeExtension {

    @Override
    public boolean isForceHighlightParents(@NotNull PsiFile file) {
        return file.getFileType() == ElmFileType.INSTANCE;
    }
}
