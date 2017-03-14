package mkolaczek.elm.features.formatting.custom;

import com.intellij.formatting.Block;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import mkolaczek.elm.features.formatting.ChopDefinition;
import mkolaczek.elm.features.formatting.ElmChoppedBlock;
import mkolaczek.elm.psi.Elements;
import org.jetbrains.annotations.NotNull;

import static mkolaczek.elm.features.formatting.ChopDefinition.chopOn;
import static mkolaczek.elm.psi.Elements.*;
import static mkolaczek.elm.psi.Tokens.LOW_VAR;

public class TypeConstructorArgsBlock extends ElmChoppedBlock {
    public TypeConstructorArgsBlock(@NotNull ASTNode node,
                                    SpacingBuilder spacing) {
        super(node, spacing, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true), chopDef());
    }

    private static ChopDefinition chopDef() {
        return chopOn(LOW_VAR, TYPE_NAME_REF, RECORD_TYPE, TUPLE_TYPE).done();
    }

    @NotNull
    @Override
    protected Block createBlock(ASTNode node, int i) {
        if (node.getElementType() == Elements.RECORD_TYPE || node.getElementType() == Elements.TUPLE_TYPE) {
            return new SurroundedBlock(node, spacing, myWrap, true);
        }
        return super.createBlock(node, i);
    }
}
