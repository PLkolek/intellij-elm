package mkolaczek.elm.features.inspections;

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
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.CommonProcessors;
import mkolaczek.elm.features.ElmFindUsagesProvider;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.*;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.function.Function.identity;

public class UnusedDeclarationInspection extends LocalInspectionTool {

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Unused declaration";
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
        Stream<TypeDeclaration> types = module.declarations(TypeOfDeclaration.TYPE);
        Stream<TypeConstructor> constructors = module.declarations(TypeOfDeclaration.TYPE)
                                                     .flatMap(TypeDeclaration::constructors);
        Stream<OperatorDeclaration> operators = module.declarations(TypeOfDeclaration.OPERATOR);
        Stream<PsiNameIdentifierOwner> toCheck = Stream.of(Stream.of(module), types, constructors, operators)
                                                       .flatMap(identity());
        return toCheck
                .filter(e -> !isNullOrEmpty(e.getName()))
                .map(e -> check(manager, file, e))
                .filter(Objects::nonNull)
                .toArray(ProblemDescriptor[]::new);
    }

    public ProblemDescriptor check(InspectionManager manager, PsiFile file, PsiNameIdentifierOwner element) {
        return descriptor(manager, element, findUsages(file, element));
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

}
