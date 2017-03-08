// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.google.common.collect.Lists;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.Tokens;
import mkolaczek.util.Optionals;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.nextVisibleLeaf;
import static com.intellij.psi.util.PsiTreeUtil.prevVisibleLeaf;
import static java.util.stream.Collectors.toList;

public class ModuleValueList extends ASTWrapperPsiElement {

    public ModuleValueList(ASTNode node) {
        super(node);
    }

    public <T extends PsiElement> Collection<T> values(Class<T> nodeType) {
        return PsiTreeUtil.findChildrenOfType(this, nodeType);
    }

    public boolean isOpenListing() {
        return PsiTreeUtil.getChildOfType(this, OpenListing.class) != null;
    }

    public Collection<TypeExport> exportedTypes() {
        return values(ExportedValue.class).stream()
                                          .map(ExportedValue::typeExport)
                                          .flatMap(Optionals::stream)
                                          .collect(toList());
    }

    public void deleteComma(@NotNull PsiElement listedValue) {
        List<? extends PsiElement> values = Lists.newArrayList(values(listedValue.getClass()));
        int index = values.indexOf(listedValue);
        if (values.size() > 1) {
            PsiElement comma = index > 0 ? prevVisibleLeaf(listedValue) : nextVisibleLeaf(listedValue);
            if (comma != null && comma.getNode().getElementType() == Tokens.COMMA) {
                comma.delete();
            }
        }
    }
}
