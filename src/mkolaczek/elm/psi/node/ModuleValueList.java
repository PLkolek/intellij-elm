// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.util.Streams;

import java.util.Collection;
import java.util.Optional;

import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;
import static java.util.stream.Collectors.toList;
import static mkolaczek.util.Streams.stream;

public class ModuleValueList extends ASTWrapperPsiElement {

    public ModuleValueList(ASTNode node) {
        super(node);
    }

    public boolean isOpenListing() {
        return PsiTreeUtil.getChildOfType(this, OpenListing.class) != null;
    }

    public Optional<CommaSeparatedList> valuesList() {
        return Optional.ofNullable(getChildOfType(this, CommaSeparatedList.class));
    }

    public Collection<TypeExport> exportedTypes() {
        return values(ExportedValue.class).stream()
                                          .map(ExportedValue::typeExport)
                                          .flatMap(Streams::stream)
                                          .collect(toList());
    }

    public <T extends PsiElement> Collection<T> values(Class<T> nodeType) {
        return stream(valuesList()).flatMap(l -> l.values(nodeType).stream()).collect(toList());
    }

}
