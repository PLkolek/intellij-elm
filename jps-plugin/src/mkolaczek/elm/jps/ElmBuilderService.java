package mkolaczek.elm.jps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ElmBuilderService extends BuilderService {
    @NotNull
    @Override
    public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return Collections.singletonList(new ElmBuilder());
    }

    @NotNull
    @Override
    public List<ElmTargetType> getTargetTypes() {
        return Arrays.asList(ElmTargetType.PRODUCTION, ElmTargetType.TESTS);
    }

}