// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.node.*;

public interface Elements {

    Element DECLARATION = new Element("DECLARATION");
    Element DECLARATIONS = new Element("DECLARATIONS");
    Element DOC_COMMENT = new Element("DOC_COMMENT");
    Element DOTTED_CAP_VAR = new Element("DOTTED_CAP_VAR");
    Element EXPORTS = new Element("EXPORTS");
    Element FRESH_DECLARATION = new Element("FRESH_DECLARATION");
    Element IMPORT_2 = new Element("IMPORT_2");
    Element IMPORT_METHOD = new Element("IMPORT_METHOD");
    Element LISTING = new Element("LISTING");
    Element MODULE_DECLARATION = new Element("MODULE_DECLARATION");
    Element MODULE_HEADER = new Element("MODULE_HEADER");
    Element MODULE_VALUE = new Element("MODULE_VALUE");
    Element MODULE_VALUE_LIST = new Element("MODULE_VALUE_LIST");
    Element MULTILINE_COMMENT = new Element("MULTILINE_COMMENT");
    Element PADDED_COMMA = new Element("PADDED_COMMA");
    Element PADDED_EQUALS = new Element("PADDED_EQUALS");
    Element SYMBOL_OP = new Element("SYMBOL_OP");
    Element TERM = new Element("TERM");
    Element TUPLE = new Element("TUPLE");
    Element TUPLE_CTOR = new Element("TUPLE_CTOR");
    Element TYPE_ALIAS = new Element("TYPE_ALIAS");
    Element TYPE_APP = new Element("TYPE_APP");
    Element TYPE_DECLARATION = new Element("TYPE_DECLARATION");
    Element TYPE_EXPORT = new Element("TYPE_EXPORT");
    Element TYPE_EXPR = new Element("TYPE_EXPR");
    Element VAR = new Element("VAR");
    Element MODULE_NAME = new Element("MODULE_NAME");
    Element MODULE_NAME_REF = new Element("MODULE_NAME_REF");
    Element MODULE_ALIAS = new Element("MODULE_ALIAS");
    Element MODULE_NODE = new Element("MODULE_NODE");
    Element EXPORTED_VALUE = new Element("EXPORTED_VALUE");
    Element EXPOSING_NODE = new Element("EXPOSING_NODE");
    Element EFFECT_MODULE_SETTINGS = new Element("EFFECT_MODULE_SETTINGS");
    Element EFFECT_MODULE_SETTINGS_LIST = new Element("EFFECT_MODULE_SETTINGS_LIST");
    Element EFFECT_MODULE_SETTING = new Element("EFFECT_MODULE_SETTING");
    Element OPERATOR = new Element("OPERATOR");
    Element OPEN_LISTING_NODE = new Element("OPEN_LISTING_NODE");
    Element IMPORTS = new Element("IMPORTS");

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
