package mkolaczek.elm.jps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;

import java.util.ArrayList;
import java.util.List;

public class ElmTargetType extends ModuleBasedBuildTargetType<ElmTarget> {
    public static final ElmTargetType PRODUCTION = new ElmTargetType("elm-production", false);
    public static final ElmTargetType TESTS = new ElmTargetType("elm-tests", true);
    private final boolean test;

    public ElmTargetType(String name, boolean inTest) {
        super(name);
        test = inTest;
    }

    @NotNull
    @Override
    public List<ElmTarget> computeAllTargets(@NotNull JpsModel jpsModel) {
        List<ElmTarget> targets = new ArrayList<>();
        for (JpsTypedModule<JpsDummyElement> module : jpsModel.getProject().getModules(JpsElmModuleType.INSTANCE)) {
            targets.add(new ElmTarget(module, this));
        }
        return targets;
    }

    @NotNull
    @Override
    public BuildTargetLoader<ElmTarget> createLoader(@NotNull JpsModel jpsModel) {
        return new BuildTargetLoader<ElmTarget>() {
            @Nullable
            @Override
            public ElmTarget createTarget(@NotNull String targetId) {
                for (JpsTypedModule<JpsDummyElement> module : jpsModel.getProject()
                                                                      .getModules(JpsElmModuleType.INSTANCE)) {
                    if (module.getName().equals(targetId)) {
                        return new ElmTarget(module, ElmTargetType.this);
                    }
                }
                return null;
            }
        };
    }

    public boolean isTests() {
        return test;
    }
}