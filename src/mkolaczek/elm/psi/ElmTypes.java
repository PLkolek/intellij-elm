// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.node.*;

public interface ElmTypes {

    IElementType CAP_VAR_2 = new ElmElementType("CAP_VAR_2");
    IElementType DECLARATION = new ElmElementType("DECLARATION");
    IElementType DECLARATIONS = new ElmElementType("DECLARATIONS");
    IElementType DOC_COMMENT = new ElmElementType("DOC_COMMENT");
    IElementType DOTTED_CAP_VAR = new ElmElementType("DOTTED_CAP_VAR");
    IElementType EXPORTS = new ElmElementType("EXPORTS");
    IElementType FRESH_DECLARATION = new ElmElementType("FRESH_DECLARATION");
    IElementType IMPORT_2 = new ElmElementType("IMPORT_2");
    IElementType IMPORT_METHOD = new ElmElementType("IMPORT_METHOD");
    IElementType LISTING = new ElmElementType("LISTING");
    IElementType MODULE_DECLARATION = new ElmElementType("MODULE_DECLARATION");
    IElementType MODULE_HEADER = new ElmElementType("MODULE_HEADER");
    IElementType MODULE_VALUE = new ElmElementType("MODULE_VALUE");
    IElementType MODULE_VALUE_LIST = new ElmElementType("MODULE_VALUE_LIST");
    IElementType MULTILINE_COMMENT = new ElmElementType("MULTILINE_COMMENT");
    IElementType PADDED_COMMA = new ElmElementType("PADDED_COMMA");
    IElementType PADDED_EQUALS = new ElmElementType("PADDED_EQUALS");
    IElementType SYMBOL_OP = new ElmElementType("SYMBOL_OP");
    IElementType TERM = new ElmElementType("TERM");
    IElementType TUPLE = new ElmElementType("TUPLE");
    IElementType TUPLE_CTOR = new ElmElementType("TUPLE_CTOR");
    IElementType TYPE_ALIAS = new ElmElementType("TYPE_ALIAS");
    IElementType TYPE_APP = new ElmElementType("TYPE_APP");
    IElementType TYPE_DECLARATION = new ElmElementType("TYPE_DECLARATION");
    IElementType TYPE_EXPORT = new ElmElementType("TYPE_EXPORT");
    IElementType TYPE_EXPR = new ElmElementType("TYPE_EXPR");
    IElementType VAR = new ElmElementType("VAR");
    IElementType MODULE_NAME = new ElmElementType("MODULE_NAME");
    IElementType MODULE_NAME_REF = new ElmElementType("MODULE_NAME_REF");
    IElementType MODULE_ALIAS = new ElmElementType("MODULE_ALIAS");

    IElementType ALIAS = new ElmTokenType("ALIAS");
    IElementType ARROW = new ElmTokenType("ARROW");
    IElementType AS = new ElmTokenType("AS");
    IElementType BEGIN_COMMENT = new ElmTokenType("BEGIN_COMMENT");
    IElementType BEGIN_DOC_COMMENT = new ElmTokenType("BEGIN_DOC_COMMENT");
    IElementType CAP_VAR = new ElmTokenType("CAP_VAR");
    IElementType COLON = new ElmTokenType("COLON");
    IElementType COMMA = new ElmTokenType("COMMA");
    IElementType COMMA_OP = new ElmTokenType("COMMA_OP");
    IElementType COMMENT = new ElmTokenType("COMMENT");
    IElementType COMMENT_CONTENT = new ElmTokenType("COMMENT_CONTENT");
    IElementType DOT = new ElmTokenType("DOT");
    IElementType END_COMMENT = new ElmTokenType("END_COMMENT");
    IElementType EQUALS = new ElmTokenType("EQUALS");
    IElementType EXPOSING = new ElmTokenType("EXPOSING");
    IElementType IMPORT = new ElmTokenType("IMPORT");
    IElementType LOW_VAR = new ElmTokenType("LOW_VAR");
    IElementType LPAREN = new ElmTokenType("LPAREN");
    IElementType MODULE = new ElmTokenType("MODULE");
    IElementType OPEN_LISTING = new ElmTokenType("OPEN_LISTING");
    IElementType OR = new ElmTokenType("OR");
    IElementType RPAREN = new ElmTokenType("RPAREN");
    IElementType SYM_OP = new ElmTokenType("SYM_OP");
    IElementType TYPE = new ElmTokenType("TYPE");
    IElementType WHERE = new ElmTokenType("WHERE");
    IElementType WHITE_SPACE = new ElmTokenType("WHITE_SPACE");
    IElementType NEW_LINE = new ElmTokenType("NEW_LINE");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == DECLARATION) {
                return new ElmDeclaration(node);
            } else if (type == DECLARATIONS) {
                return new ElmDeclarations(node);
            } else if (type == DOC_COMMENT) {
                return new ElmDocComment(node);
            } else if (type == DOTTED_CAP_VAR) {
                return new ElmDottedCapVar(node);
            } else if (type == EXPORTS) {
                return new ElmExports(node);
            } else if (type == FRESH_DECLARATION) {
                return new ElmFreshDeclaration(node);
            } else if (type == IMPORT_2) {
                return new ElmImport2(node);
            } else if (type == IMPORT_METHOD) {
                return new ElmImportMethod(node);
            } else if (type == LISTING) {
                return new ElmListing(node);
            } else if (type == MODULE_DECLARATION) {
                return new ElmModuleDeclaration(node);
            } else if (type == MODULE_HEADER) {
                return new ElmModuleHeader(node);
            } else if (type == MODULE_VALUE) {
                return new ElmModuleValue(node);
            } else if (type == MODULE_VALUE_LIST) {
                return new ElmModuleValueList(node);
            } else if (type == MULTILINE_COMMENT) {
                return new ElmMultilineComment(node);
            } else if (type == PADDED_COMMA) {
                return new ElmPaddedComma(node);
            } else if (type == PADDED_EQUALS) {
                return new ElmPaddedEquals(node);
            } else if (type == SYMBOL_OP) {
                return new ElmSymbolOp(node);
            } else if (type == TERM) {
                return new ElmTerm(node);
            } else if (type == TUPLE) {
                return new ElmTuple(node);
            } else if (type == TUPLE_CTOR) {
                return new ElmTupleCtor(node);
            } else if (type == TYPE_ALIAS) {
                return new ElmTypeAlias(node);
            } else if (type == TYPE_APP) {
                return new ElmTypeApp(node);
            } else if (type == TYPE_DECLARATION) {
                return new ElmTypeDeclaration(node);
            } else if (type == TYPE_EXPORT) {
                return new ElmTypeExport(node);
            } else if (type == TYPE_EXPR) {
                return new ElmTypeExpr(node);
            } else if (type == VAR) {
                return new ElmVar(node);
            } else if (type == MODULE_NAME_REF) {
                return new ElmModuleNameRef(node);
            } else if (type == MODULE_NAME) {
                return new ElmModuleName(node);
            } else if (type == MODULE_ALIAS) {
                return new ElmModuleAlias(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
