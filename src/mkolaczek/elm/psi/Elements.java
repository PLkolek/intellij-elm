// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.node.*;

public interface Elements {

    IElementType DECLARATION = new Element("DECLARATION");
    IElementType DECLARATIONS = new Element("DECLARATIONS");
    IElementType DOC_COMMENT = new Element("DOC_COMMENT");
    IElementType DOTTED_CAP_VAR = new Element("DOTTED_CAP_VAR");
    IElementType EXPORTS = new Element("EXPORTS");
    IElementType FRESH_DECLARATION = new Element("FRESH_DECLARATION");
    IElementType IMPORT_2 = new Element("IMPORT_2");
    IElementType IMPORT_METHOD = new Element("IMPORT_METHOD");
    IElementType LISTING = new Element("LISTING");
    IElementType MODULE_DECLARATION = new Element("MODULE_DECLARATION");
    IElementType MODULE_HEADER = new Element("MODULE_HEADER");
    IElementType MODULE_VALUE = new Element("MODULE_VALUE");
    IElementType MODULE_VALUE_LIST = new Element("MODULE_VALUE_LIST");
    IElementType MULTILINE_COMMENT = new Element("MULTILINE_COMMENT");
    IElementType PADDED_COMMA = new Element("PADDED_COMMA");
    IElementType PADDED_EQUALS = new Element("PADDED_EQUALS");
    IElementType SYMBOL_OP = new Element("SYMBOL_OP");
    IElementType TERM = new Element("TERM");
    IElementType TUPLE = new Element("TUPLE");
    IElementType TUPLE_CTOR = new Element("TUPLE_CTOR");
    IElementType TYPE_ALIAS = new Element("TYPE_ALIAS");
    IElementType TYPE_APP = new Element("TYPE_APP");
    IElementType TYPE_DECLARATION = new Element("TYPE_DECLARATION");
    IElementType TYPE_EXPORT = new Element("TYPE_EXPORT");
    IElementType TYPE_EXPR = new Element("TYPE_EXPR");
    IElementType VAR = new Element("VAR");
    IElementType MODULE_NAME = new Element("MODULE_NAME");
    IElementType MODULE_NAME_REF = new Element("MODULE_NAME_REF");
    IElementType MODULE_ALIAS = new Element("MODULE_ALIAS");
    IElementType MODULE_NODE = new Element("MODULE_NODE");
    IElementType EXPORTED_VALUE = new Element("EXPORTED_VALUE");
    IElementType EXPOSING_NODE = new Element("EXPOSING_NODE");
    IElementType EFFECT_MODULE_SETTINGS = new Element("EFFECT_MODULE_SETTINGS");
    IElementType EFFECT_MODULE_SETTINGS_LIST = new Element("EFFECT_MODULE_SETTINGS_LIST");
    IElementType EFFECT_MODULE_SETTING = new Element("EFFECT_MODULE_SETTING");
    IElementType OPERATOR = new Element("OPERATOR");
    IElementType OPEN_LISTING_NODE = new Element("OPEN_LISTING_NODE");
    IElementType IMPORTS = new Element("IMPORTS");

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
            } else if (type == MODULE_NODE) {
                return new ElmModule(node);
            } else if (type == EXPORTED_VALUE) {
                return new ElmExportedValue(node);
            } else if (type == EXPOSING_NODE) {
                return new ElmExposingNode(node);
            } else if (type == OPERATOR) {
                return new ElmOperator(node);
            } else if (type == OPEN_LISTING_NODE) {
                return new ElmOpenListing(node);
            } else if (type == IMPORTS) {
                return new ElmImports(node);
            } else if (type == EFFECT_MODULE_SETTINGS) {
                return new EffectModuleSettings(node);
            } else if (type == EFFECT_MODULE_SETTINGS_LIST) {
                return new EffectModuleSettingsList(node);
            } else if (type == EFFECT_MODULE_SETTING) {
                return new EffectModuleSetting(node);
            }
            else {

                throw new AssertionError("Unknown element type: " + type);
            }
        }
    }
}
