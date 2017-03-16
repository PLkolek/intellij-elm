package mkolaczek.elm.features.inspections;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.intellij.codeInspection.*;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.find.findUsages.JavaFindUsagesHelper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceUsageInfo;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.CommonProcessors;
import mkolaczek.elm.features.ElmFindUsagesProvider;
import mkolaczek.elm.features.refactoring.safeDelete.ElmSafeDeleteDelegate;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.ExposingNode;
import mkolaczek.elm.psi.node.Module;
import mkolaczek.elm.psi.node.TypeConstructor;
import mkolaczek.elm.psi.node.TypeDeclaration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.stream.Collectors.toList;

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
        Module module = ((ElmFile) file).module();
        if (module == null) {
            return ProblemDescriptor.EMPTY_ARRAY;
        }
        Collection<TypeDeclaration> types = module.typeDeclarations().collect(toList());
        List<PsiNameIdentifierOwner> toCheck = Lists.newArrayList(module);
        List<TypeConstructor> constructors = types.stream().flatMap(TypeDeclaration::constructors).collect(toList());
        toCheck.addAll(types);
        toCheck.addAll(constructors);

        return toCheck.stream()
                      .map(e -> check(manager, file, e))
                      .filter(Objects::nonNull)
                      .toArray(ProblemDescriptor[]::new);
    }

    public ProblemDescriptor check(InspectionManager manager, PsiFile file, PsiNameIdentifierOwner element) {
        if (element == null || Strings.isNullOrEmpty(element.getName())) {
            return null;
        }
        CommonProcessors.CollectProcessor<UsageInfo> collector = findUsages(file, element);
        return descriptor(manager, element, collector);
    }

    private ProblemDescriptor descriptor(@NotNull InspectionManager manager,
                                         PsiNameIdentifierOwner element,
                                         CommonProcessors.CollectProcessor<UsageInfo> collector) {
        PsiElement name = element.getNameIdentifier();
        if (collector.getResults().isEmpty() && name != null) {
            return manager.createProblemDescriptor(name,
                    (TextRange) null,
                    "Unused " + ElmFindUsagesProvider.type(element),
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    true,
                    new RemoveElementQuickFix());

        }
        return null;
    }

    @NotNull
    private CommonProcessors.CollectProcessor<UsageInfo> findUsages(@NotNull PsiFile file, PsiElement element) {
        FindUsagesOptions options = new FindUsagesOptions(file.getProject());
        options.isUsages = true;
        CommonProcessors.CollectProcessor<UsageInfo> collector = new CommonProcessors.CollectProcessor<UsageInfo>() {
            @Override
            protected boolean accept(UsageInfo info) {
                PsiFile psiFile = info.getFile();
                if (psiFile == null) {
                    return false; // ignore usages in currentFile
                }
                int offset = info.getNavigationOffset();
                if (offset == -1) {
                    return false;
                }
                PsiElement usage = psiFile.findElementAt(offset);
                assert usage != null;
                boolean outsideSelf = getParentOfType(usage, element.getClass()) != element;
                //same as in ElmSafeDeleteDelegate, sorry
                boolean outsideExposing = getParentOfType(usage, ExposingNode.class) == null;
                return !(usage instanceof PsiComment) && outsideSelf && outsideExposing; // ignore comments
            }
        };

        JavaFindUsagesHelper.processElementUsages(element, options, collector);
        return collector;
    }

    private static class RemoveElementQuickFix implements LocalQuickFix {

        @Nls
        @NotNull
        @Override
        public String getName() {
            return "Remove";
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiNameIdentifierOwner toDelete = getParentOfType(descriptor.getPsiElement(),
                    PsiNameIdentifierOwner.class,
                    false);
            assert toDelete != null;
            for (UsageInfo usage : ElmSafeDeleteDelegate.usages(toDelete, new PsiElement[]{toDelete})) {
                if (usage instanceof SafeDeleteReferenceUsageInfo) {
                    ((SafeDeleteReferenceUsageInfo) usage).deleteElement();
                }
            }
            toDelete.delete();
        }
    }
}
