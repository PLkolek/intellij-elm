package mkolaczek.elm.jps;

import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties;
import org.jetbrains.jps.model.module.JpsModuleType;

public class JpsElmModuleType extends JpsElementTypeWithDummyProperties implements JpsModuleType<JpsDummyElement> {
    public static final JpsElmModuleType INSTANCE = new JpsElmModuleType();

    private JpsElmModuleType() {
    }
}