package mkolaczek.elm;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import mkolaczek.elm.parsers.MyElmParser;
import mkolaczek.elm.psi.ElmFile;
import mkolaczek.elm.psi.ElmElementTypes;
import org.jetbrains.annotations.NotNull;


public class ElmParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(Language.findInstance(ElmLanguage.class));
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);


    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new ElmLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new MyElmParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return ElmElementTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ElmFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
