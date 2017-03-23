package mkolaczek.elm.psi.node.extensions;

import com.intellij.psi.NavigatablePsiElement;

import java.util.stream.Stream;

public interface Declaration extends DocCommented, NavigatablePsiElement {

    default Stream<String> declaredValueNames() {
        return Stream.empty();
    }

    default Stream<String> declaredOperatorName() {
        return Stream.empty();
    }

}
