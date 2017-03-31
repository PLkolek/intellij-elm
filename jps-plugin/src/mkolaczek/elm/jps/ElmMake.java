package mkolaczek.elm.jps;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModuleSourceRoot;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ElmMake {
    @NotNull
    public static Process build(JpsModuleSourceRoot sourceRoot) throws ExecutionException, IOException {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setWorkDirectory(sourceRoot.getFile().getParent());
        commandLine.setExePath("elm-make");
        ParametersList parametersList = commandLine.getParametersList();
        parametersList.add("--report=json");
        parametersList.add(sourceRoot.getFile().toPath().getFileName().resolve("Main.elm").toString());
        commandLine.setRedirectErrorStream(true);
        Process process = commandLine.createProcess();
        OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
        writer.write('n');
        writer.flush();
        writer.close();
        return process;
    }

}
