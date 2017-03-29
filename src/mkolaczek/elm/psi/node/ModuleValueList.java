// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi.node;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import mkolaczek.elm.psi.node.extensions.Exposed;
import mkolaczek.elm.psi.node.extensions.TypeOfExposed;
import mkolaczek.util.Streams;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
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

    public Stream<Exposed> exposed(TypeOfExposed exposedElementsType) {
        return values(ExposedValue.class).stream()
                                         .map(e -> e.exposed(exposedElementsType))
                                         .flatMap(Streams::stream);
    }

    public <T extends PsiElement> Collection<T> values(Class<T> nodeType) {
        return stream(valuesList()).flatMap(l -> l.values(nodeType).stream()).collect(toList());
    }

    public <T extends PsiNamedElement> Optional<T> item(Class<T> type, String name) {
        return values(type).stream().filter(i -> name.equals(i.getName())).findAny();
    }

    public static boolean maybeDeleteChild(PsiElement child) {
        ExposedValue exposedValue = getParentOfType(child, ExposedValue.class);
        if (exposedValue != null) {
            exposedValue.containingList().deleteSeparator(exposedValue);
            exposedValue.delete();
        }
        return exposedValue != null;
    }
}
