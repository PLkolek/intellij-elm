package mkolaczek.elm.inspections;

import com.intellij.codeInspection.*;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.find.findUsages.JavaFindUsagesHelper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.CommonProcessors;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ElmModule;
import mkolaczek.elm.psi.node.ElmModuleName;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnusedDeclarationInspection extends LocalInspectionTool {

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Unused module";
    }

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
                    new RemoveModuleQuickFix())
            };
        }
        return ProblemDescriptor.EMPTY_ARRAY;
    }

    private static class RemoveModuleQuickFix implements LocalQuickFix {

        @Nls
        @NotNull
        @Override
        public String getName() {
            return "Remove module";
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            ((ElmModuleName) descriptor.getPsiElement()).module().delete();
        }
    }
}
