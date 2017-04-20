package mkolaczek.elm.jps;


import com.google.gson.Gson;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.jetbrains.jps.model.module.JpsModule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ElmBuilder extends ModuleLevelBuilder {
    // Messages go to the log available in Help -> Show log in finder,
    // "build-log" subdirectory.
    private final static Logger LOG = Logger.getInstance(ElmBuilder.class);
    public static final String COMPILER_NAME = "elm-make";

    public ElmBuilder() {
        super(BuilderCategory.TRANSLATOR);
    }

    @Override
    public ExitCode build(CompileContext context,
                          ModuleChunk chunk,
                          DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirtyFilesHolder,
                          OutputConsumer outputConsumer) throws ProjectBuildException {
        try {
            for (JpsModule module : chunk.getModules()) {
                // Builder gets called for pure java projects as well. Silently
                // skip those since processMessage() of error will halt the
                // entire compilation. Funny..
                if (!module.getModuleType().equals(JpsElmModuleType.INSTANCE)) {
                    continue;
                }

                Optional<File> packageJsonFile = getPackageJsonFile(module);
                if (!packageJsonFile.isPresent()) {
                    context.processMessage(new CompilerMessage("Elm", BuildMessage.Kind.ERROR,
                            "Can not find elm-package.json file in " + getContentRootPath(module)));
                    continue;
                }

                if (runBuild(context, module)) {
                    return ExitCode.ABORT;
                }
            }
            return ExitCode.OK;
        } catch (IOException | InterruptedException | ExecutionException e) {
            processCabalError(context, e);
        }
        return ExitCode.ABORT;
    }

    private static void processCabalError(CompileContext context, Exception e) {
        e.printStackTrace();
        context.processMessage(new CompilerMessage("cabal", BuildMessage.Kind.ERROR, e.getMessage()));
    }

    private static boolean runBuild(CompileContext context, JpsModule module)
            throws IOException, InterruptedException, ExecutionException {
        context.processMessage(new ProgressMessage("elm-make build"));
        context.processMessage(new CompilerMessage(COMPILER_NAME, BuildMessage.Kind.INFO, "Start build"));
        Process buildProcess = ElmMake.build(module.getSourceRoots().get(0));

        if (buildProcess.waitFor() != 0) {
            context.processMessage(new CompilerMessage(COMPILER_NAME, BuildMessage.Kind.ERROR, "build errors."));
            processOut(context, buildProcess, module);
            return true;
        }
        processOut(context, buildProcess, module);
        return false;
    }

    private static void processOut(CompileContext context, Process process, JpsModule module) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String result = readerToString(reader);
            int jsonBegin = result.indexOf("[");
            int jsonEnd = result.lastIndexOf("]");
            if (jsonBegin != -1 && jsonEnd != -1) {

                if (jsonBegin > 0) {
                    String message = result.substring(0, jsonBegin);
                    context.processMessage(new CompilerMessage(COMPILER_NAME, BuildMessage.Kind.INFO, message));
                }
                String json = result.substring(jsonBegin, jsonEnd + 1);
                for (String subJson : json.split("\n")) {
                    ErrorJson[] errors = new Gson().fromJson(subJson, ErrorJson[].class);
                    for (ErrorJson error : errors) {
                        BuildMessage.Kind kind = kind(error.type);
                        String sourcePath = getContentRootPath(module) + File.separator + error.file;
                        context.processMessage(new CompilerMessage(
                                        COMPILER_NAME,
                                        kind,
                                        error.overview + "\n" + error.details,
                                        sourcePath,
                                        -1,
                                        -1,
                                        -1,
                                        error.region.start.line,
                                        error.region.start.column
                                )
                        );
                    }
                }

                if (jsonEnd < result.length() - 1) {
                    String message = result.substring(jsonEnd + 1);
                    context.processMessage(new CompilerMessage(COMPILER_NAME, BuildMessage.Kind.INFO, message));
                }
            } else {
                String file = null;
                BuildMessage.Kind kind = BuildMessage.Kind.ERROR;
                if (result.contains("SYNTAX PROBLEM")) {
                    String[] parts = result.substring(0, result.indexOf("\n")).split("\\s+");
                    file = parts[parts.length - 1];
                    file = getContentRootPath(module) + File.separator + file;
                }
                if (result.contains("Successfully generated")) {
                    kind = BuildMessage.Kind.INFO;
                }
                context.processMessage(new CompilerMessage(COMPILER_NAME, kind, result, file));
            }
        }
    }

    private static String readerToString(Reader reader) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = reader.read();
        while (result != -1) {
            buf.write((byte) result);
            result = reader.read();
        }
        return buf.toString(StandardCharsets.UTF_8.name());
    }

    private static BuildMessage.Kind kind(String type) {
        try {
            return BuildMessage.Kind.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BuildMessage.Kind.ERROR;
        }
    }

    private static Optional<File> getPackageJsonFile(JpsModule module) {
        String pathname = getContentRootPath(module);
        //noinspection ConstantConditions
        for (File file : new File(pathname).listFiles()) {
            if (file.getName().equals("elm-package.json")) {
                return Optional.of(file);
            }
        }
        return Optional.empty();
    }

    private static String getContentRootPath(JpsModule module) {
        String url = module.getContentRootsList().getUrls().get(0);
        return url.substring("file://".length());
    }

    @Override
    public List<String> getCompilableFileExtensions() {
        return Collections.singletonList("elm");
    }

    @Override
    @NotNull
    public String getPresentableName() {
        return "elm-make";
    }

}