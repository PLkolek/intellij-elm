// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.node.*;

public interface Elements {

    Element DECLARATION = new Element("declaration");
    Element DECLARATIONS = new Element("declarations");
    Element DOC_COMMENT = new Element("DOC_COMMENT", "doc comment");
    Element IMPORT_LINE = new Element("IMPORT_LINE", "import line");
    Element MODULE_HEADER = new Element("MODULE_HEADER", "module header");
    Element MODULE_VALUE_LIST = new Element("MODULE_VALUE_LIST", "exposed values");
    Element MULTILINE_COMMENT = new Element("MULTILINE_COMMENT", "multiline comment");
    Element TYPE_DECLARATION = new Element("TYPE_DECLARATION", "type declaration");
    Element MODULE_NAME = new Element("MODULE_NAME", "module name");
    Element MODULE_NAME_REF = new Element("MODULE_NAME_REF", "module name");
    Element MODULE_ALIAS = new Element("MODULE_ALIAS", "module alias");
    Element MODULE_NODE = new Element("MODULE_NODE", "module");
    Element EXPORTED_VALUE = new Element("EXPORTED_VALUE", "exposed value");
    Element EXPOSING_NODE = new Element("EXPOSING_NODE", "exposing");
    Element EFFECT_MODULE_SETTINGS = new Element("EFFECT_MODULE_SETTINGS", "module settings");
    Element EFFECT_MODULE_SETTINGS_LIST = new Element("EFFECT_MODULE_SETTINGS_LIST", "module settings list");
    Element EFFECT_MODULE_SETTING = new Element("EFFECT_MODULE_SETTING", "module setting");
    Element OPERATOR = new Element("operator");
    Element OPEN_LISTING_NODE = new Element("OPEN_LISTING_NODE", "open listing");
    Element IMPORTS = new Element("imports");
    Element TYPE_EXPORT = new Element("TYPE_EXPORT", "type export");
    Element TYPE_CONSTRUCTOR = new Element("TYPE_CONSTRUCTOR", "type constructor");
    Element TYPE_CONSTRUCTOR_NAME = new Element("TYPE_CONSTRUCTOR_NAME", "type constructor name");
    Element TYPE_CONSTRUCTOR_REF = new Element("TYPE_CONSTRUCTOR_REF", "type constructor");
    Element TYPE_ALIAS_DECL_NODE = new Element("TYPE_ALIAS_DECL_NODE", "type alias declaration");
    Element TYPE_DECL_NODE = new Element("TYPE_DECL_NODE", "type declaration");
    Element TYPE_NAME = new Element("TYPE_NAME", "name of a type");
    Element TYPE_NAME_REF = new Element("TYPE_NAME_REF", "name of a type");
    Element RUNE_OF_AUTOCOMPLETION_EL = new Element("RUNE_OF_AUTOCOMPLETION_EL");
    Element PIPE_SEP = new Element("PIPE_SEP", "pipe separated list");
    Element COMMA_SEP = new Element("PIPE_SEP", "comma separated list");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == DECLARATION) {
                return new Declaration(node);
            } else if (type == DECLARATIONS) {
                return new Declarations(node);
            } else if (type == DOC_COMMENT) {
                return new DocComment(node);
            } else if (type == IMPORT_LINE) {
                return new Import(node);
            } else if (type == MODULE_HEADER) {
                return new ModuleHeader(node);
            } else if (type == MODULE_VALUE_LIST) {
                return new ModuleValueList(node);
            } else if (type == MULTILINE_COMMENT) {
                return new MultilineComment(node);
            } else if (type == TYPE_DECLARATION) {
                return new TypeDeclaration(node);
            } else if (type == MODULE_NAME_REF) {
                return new ModuleNameRef(node);
            } else if (type == MODULE_NAME) {
                return new ModuleName(node);
            } else if (type == MODULE_ALIAS) {
                return new ModuleAlias(node);
            } else if (type == MODULE_NODE) {
                return new Module(node);
            } else if (type == EXPORTED_VALUE) {
                return new ExportedValue(node);
            } else if (type == EXPOSING_NODE) {
                return new ExposingNode(node);
            } else if (type == OPERATOR) {
                return new Operator(node);
            } else if (type == OPEN_LISTING_NODE) {
                return new OpenListing(node);
            } else if (type == IMPORTS) {
                return new Imports(node);
            } else if (type == EFFECT_MODULE_SETTINGS) {
                return new EffectModuleSettings(node);
            } else if (type == EFFECT_MODULE_SETTINGS_LIST) {
                return new EffectModuleSettingsList(node);
            } else if (type == EFFECT_MODULE_SETTING) {
                return new EffectModuleSetting(node);
            } else if (type == TYPE_EXPORT) {
                return new TypeExport(node);
            } else if (type == TYPE_CONSTRUCTOR) {
                return new TypeConstructor(node);
            } else if (type == TYPE_CONSTRUCTOR_NAME) {
                return new TypeConstructorName(node);
            } else if (type == TYPE_CONSTRUCTOR_REF) {
                return new TypeConstructorRef(node);
            } else if (type == TYPE_ALIAS_DECL_NODE) {
                return new TypeAliasDeclNode(node);
            } else if (type == TYPE_DECL_NODE) {
                return new TypeDeclNode(node);
            } else if (type == TYPE_NAME) {
                return new TypeName(node);
            } else if (type == TYPE_NAME_REF) {
                return new TypeNameRef(node);
            } else if (type == COMMA_SEP) {
                return new CommaSeparatedList(node);
            } else if (type == PIPE_SEP) {
                return new PipeSeparatedList(node);
            } else if (type == RUNE_OF_AUTOCOMPLETION_EL) {
                return new RuneOfAutocompletion(node);
            } else {
                throw new AssertionError("Unknown element type: " + type);
            }
        }
    }
}
