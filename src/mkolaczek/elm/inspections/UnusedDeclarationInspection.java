package mkolaczek.elm.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.find.findUsages.JavaFindUsagesHelper;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.CommonProcessors;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ElmModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnusedDeclarationInspection extends LocalInspectionTool {

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file,
                                         @NotNull InspectionManager manager,
                                         boolean isOnTheFly) {
        if (!(file instanceof ElmFile)) {
            return null;
        }
        ElmModule module = ((ElmFile) file).module();
        FindUsagesOptions options = new FindUsagesOptions(file.getProject());
        options.isUsages = true;
        CommonProcessors.CollectProcessor<UsageInfo> collector = new CommonProcessors.CollectProcessor<UsageInfo>() {
            @Override
            protected boolean accept(UsageInfo info) {
                PsiFile psiFile = info.getFile();
                if (psiFile == file || psiFile == null) {
                    return false; // ignore usages in currentFile
                }
                int offset = info.getNavigationOffset();
                if (offset == -1) {
                    return false;
                }
                PsiElement element = psiFile.findElementAt(offset);
                return !(element instanceof PsiComment); // ignore comments
            }
        };

        JavaFindUsagesHelper.processElementUsages(module, options, collector);
        PsiElement name = module.getNameIdentifier();
        if (collector.getResults().isEmpty() && name != null) {
            return new ProblemDescriptor[]{manager.createProblemDescriptor(name,
                    (TextRange) null,
                    "Unused module",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    true,
                    null)
            };
        }
        return ProblemDescriptor.EMPTY_ARRAY;
    }
}
