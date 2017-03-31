package mkolaczek.elm.jps;

import org.jetbrains.jps.builders.BuildRootDescriptor;
import org.jetbrains.jps.builders.BuildTarget;

import java.io.File;

public class ElmSourceRootDescriptor extends BuildRootDescriptor {
    private final File root;
    private final ElmTarget target;

    public ElmSourceRootDescriptor(File inRoot, ElmTarget inTarget) {
        root = inRoot;
        target = inTarget;
    }

    @Override
    public String getRootId() {
        return "SourceRootDescriptor";
    }

    @Override
    public File getRootFile() {
        return root;
    }

    @Override
    public BuildTarget<?> getTarget() {
        return target;
    }
}
