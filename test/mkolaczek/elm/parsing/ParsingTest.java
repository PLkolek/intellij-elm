package mkolaczek.elm.parsing;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.testFramework.ParsingTestCase;
import mkolaczek.elm.ElmParserDefinition;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;

public class ParsingTest extends ParsingTestCase {
    public ParsingTest() {
        super("", "elm", new ElmParserDefinition());
    }

    public void testWeirdModule() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "testdata/parsing";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    @Override
    protected String loadFile(@NonNls String name) throws IOException {
        //do not trim the file!
        return FileUtil.loadFile(new File(myFullDataPath, name), CharsetToolkit.UTF8, true);
    }


}

