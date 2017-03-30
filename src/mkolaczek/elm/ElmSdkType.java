package mkolaczek.elm;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import mkolaczek.elm.boilerplate.ElmIcon;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ElmSdkType extends SdkType {
    public ElmSdkType() {
        super("elm");
    }

    @Nullable
    @Override
    public String suggestHomePath() {
        return "/usr/bin";
    }

    @Override
    public boolean isValidSdkHome(String s) {
        VirtualFile home = LocalFileSystem.getInstance().findFileByIoFile(new File(s));
        if (home != null && home.exists() && home.isDirectory()) {
            VirtualFile make = home.findChild("elm-make");
            VirtualFile package_ = home.findChild("elm-package");
            VirtualFile elm = home.findChild("elm");
            return make != null && !make.isDirectory()
                    && package_ != null && !package_.isDirectory()
                    && elm != null && !elm.isDirectory();
        }

        return false;
    }

    @Override
    public String suggestSdkName(String s, String s2) {
        return "Elm SDK";
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel,
                                                                       @NotNull SdkModificator sdkModificator) {
        return null;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Elm SDK";
    }

    @Override
    public Icon getIcon() {
        return ElmIcon.FILE;
    }

    @NotNull
    @Override
    public Icon getIconForAddAction() {
        return getIcon();
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData additionalData, @NotNull Element additional) {
    }

    public static SdkTypeId getInstance() {
        return SdkType.findInstance(ElmSdkType.class);
    }

    @Nullable
    @Override
    public String getVersionString(@NotNull Sdk sdk) {
        return executeCommand(sdk, null, "elm", "-v");
    }

    @Nullable
    private static String executeCommand(@NotNull Sdk sdk, File location, String... commandAndParams) {
        String path = sdk.getHomePath();
        if (path == null) {
            return null;
        }

        try {
            commandAndParams[0] = path + "/" + commandAndParams[0];
            ProcessBuilder processBuilder = new ProcessBuilder(commandAndParams);
            if (location != null) {
                processBuilder.directory(location);
            }
            Process process = processBuilder.start();
            process.waitFor();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.readLine();
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    static void prepareModule(Sdk sdk, Module module) {
        Path moduleDir = Paths.get(module.getModuleFilePath()).getParent();
        executeCommand(sdk, moduleDir.toFile(), "elm-package", "install", "-y");


        Path packageJson = moduleDir.resolve("elm-package.json");
        Charset charset = StandardCharsets.UTF_8;

        String content;
        try {
            content = new String(Files.readAllBytes(packageJson), charset);
            content = content.replaceAll("\"\\.\"", "\"src\"");
            Files.write(packageJson, content.getBytes(charset));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }
}