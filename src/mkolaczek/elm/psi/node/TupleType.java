package mkolaczek.elm.psi.node;

import com.intellij.lang.ASTNode;
import mkolaczek.elm.psi.node.extensions.Surrounded;

public class TupleType extends Surrounded {
    public TupleType(ASTNode node) {
        super(node);
    }
}
