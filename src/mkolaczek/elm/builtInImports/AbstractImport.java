package mkolaczek.elm.builtInImports;

import mkolaczek.elm.psi.node.extensions.HasExposing;

import java.util.Optional;

public interface AbstractImport extends HasExposing {
    Optional<String> moduleNameString();
}
