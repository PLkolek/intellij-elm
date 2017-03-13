package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Surrounded;

public class RecordType extends Surrounded {
    public RecordType(ASTNode node) {
        super(node);
    }
}
