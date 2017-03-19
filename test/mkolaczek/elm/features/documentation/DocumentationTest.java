package mkolaczek.elm.features.documentation;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import mkolaczek.elm.TestUtil;
import mkolaczek.elm.features.ElmDocumentationProvider;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.node.PortDeclaration;
import mkolaczek.elm.psi.node.extensions.TypeOfDeclaration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class DocumentationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return TestUtil.testDataPath(getClass());
    }

    public void testModuleQuickNavigateInfo() {
        String expected = "effect module Test1.B\n" +
                "&nbsp&nbsp&nbsp&nbsp where { a = A }\n" +
                "&nbsp&nbsp&nbsp&nbsp exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, ( ** ), (<:), A( .. ), B(Abc, Def, Ghci),\n" +
                "&nbsp&nbsp&nbsp&nbsp C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))";
        quickNavigateTest(expected, ElmFile::module);
    }

    public void testModuleQuickDocumentation() {
        String expected =
                "<pre>effect module Test1.B\n" +
                        "&nbsp&nbsp&nbsp&nbsp where { a = A }\n" +
                        "&nbsp&nbsp&nbsp&nbsp exposing (aaaaaaa, bbbbbbb, aaaaaaaaa, abbjaaal, ( ** ), (<:), A( .. ), B(Abc, Def, Ghci),\n" +
                        "&nbsp&nbsp&nbsp&nbsp C(Abc, Def, Ghci, Xxxxxx, Aaaaa, Bvvvvvvvvvvvvvvvvv))</pre>" +
                        "<p>{-|\n" +
                        "{- -}\n\n" +
                        "sdsd\n" +
                        "-}</p>";
        docTest(expected, ElmFile::module);
    }

    public void testTypeQuickNavigateInfo() {
        String expected = "type B = Abc | Def | Ghci";
        quickNavigateTest(expected, file -> file.module().declarations(TypeOfDeclaration.TYPE, "B")
                                                .findAny()
                                                .get());
    }

    public void testTypeQuickDocumentation() {
        docTest("<pre>type B = Abc | Def | Ghci</pre><p>{-| Test type -}</p>",
                file -> file.module().declarations(TypeOfDeclaration.TYPE, "B")
                            .findAny()
                            .get()
        );

    }

    public void testTypeConstructorQuickNavigateInfo() {
        String expected = "type B = Abc | Def | Ghci";
        quickNavigateTest(expected, file -> file.module().declarations(TypeOfDeclaration.TYPE, "B")
                                                .findAny()
                                                .get()
                                                .constructor("Ghci")
                                                .get());
    }

    public void testTypeConstructorDocumentation() {
        docTest(
                "<pre>type B = Abc | Def | Ghci</pre><p>{-| Test type -}</p>",
                file -> file.module().declarations(TypeOfDeclaration.TYPE, "B")
                            .findAny()
                            .get()
                            .constructor("Ghci")
                            .get()
        );
    }

    public void testOperatorQuickNavigateInfo() {
        String expected = "infix 3 &lt;:";
        quickNavigateTest(expected, file -> file.module().declarations(TypeOfDeclaration.OPERATOR, "<:")
                                                .findFirst().get());
    }

    public void testOperatorDocumentation() {
        docTest(
                "<pre>infix 3 &lt;:</pre><p>{-| Test operator -}</p>",
                file -> file.module().declarations(TypeOfDeclaration.OPERATOR, "<:")
                            .findFirst().get()
        );
    }

    public void testPortQuickNavigateInfo() {
        String expected = "port somePort : Cmd Int";
        quickNavigateTest(expected, this::port);
    }

    public void testPortDocumentation() {
        docTest("<pre>port somePort : Cmd Int</pre><p>{-| Test port -}</p>", this::port);
    }

    @NotNull
    private PortDeclaration port(ElmFile file) {
        return file.module().declarations(TypeOfDeclaration.PORT, "somePort")
                   .findFirst().get();
    }

    private void docTest(String expectedText, Function<ElmFile, PsiElement> elemProvider) {
        doTest(expectedText, elemProvider);
    }

    private void doTest(String expectedText, Function<ElmFile, PsiElement> elemProvider) {
        myFixture.configureByFile("Test.elm");
        PsiElement target = elemProvider.apply(((ElmFile) myFixture.getFile()));
        String info = new ElmDocumentationProvider().generateDoc(target, null);
        String expected = wrapinHtml(expectedText);
        assertThat(info, is(expected));
    }


    private void quickNavigateTest(String expectedText, Function<ElmFile, PsiElement> elemProvider) {
        myFixture.configureByFile("Test.elm");
        PsiElement target = elemProvider.apply(((ElmFile) myFixture.getFile()));
        String info = new ElmDocumentationProvider().getQuickNavigateInfo(target, null);
        String expected = wrapinHtml(expectedText);
        assertThat(info, is(expected));
    }

    @NotNull
    private String wrapinHtml(String text) {
        return "<html><head></head><body>" +
                text +
                "</body></html>";
    }

}
