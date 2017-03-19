// This is a generated file. Not intended for manual editing.
package mkolaczek.elm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import mkolaczek.elm.psi.node.*;

public interface Elements {

    Element DECLARATIONS = new Element("declarations");
    Element DOC_COMMENT = new Element("doc comment");
    Element IMPORT_LINE = new Element("import line");
    Element MODULE_HEADER = new Element("module header");
    Element MODULE_VALUE_LIST = new Element("MODULE_VALUE_LIST", "exposed values");
    Element MULTILINE_COMMENT = new Element("multiline comment");
    Element TYPE_DECLARATION = new Element("type declaration");
    Element MODULE_NAME = new Element("module name");
    Element MODULE_NAME_REF = new Element("MODULE_NAME_REF", "module name");
    Element MODULE_ALIAS = new Element("module alias");
    Element MODULE_NODE = new Element("MODULE_NODE", "module");
    Element EXPORTED_VALUE = new Element("EXPORTED_VALUE", "exposed value");
    Element EXPOSING_NODE = new Element("EXPOSING_NODE", "exposing");
    Element EFFECT_MODULE_SETTINGS = new Element("EFFECT_MODULE_SETTINGS", "module settings");
    Element EFFECT_MODULE_SETTINGS_LIST = new Element("EFFECT_MODULE_SETTINGS_LIST", "module settings list");
    Element EFFECT_MODULE_SETTING = new Element("EFFECT_MODULE_SETTING", "module setting");
    Element OPEN_LISTING_NODE = new Element("OPEN_LISTING_NODE", "open listing");
    Element IMPORTS = new Element("imports");
    Element TYPE_EXPORT = new Element("type export");
    Element VALUE_EXPORT = new Element("value export");
    Element TYPE_CONSTRUCTOR = new Element("type constructor");
    Element TYPE_CONSTRUCTOR_NAME = new Element("type constructor name");
    Element TYPE_CONSTRUCTOR_REF = new Element("TYPE_CONSTRUCTOR_REF", "type constructor");
    Element TYPE_ALIAS_DECL_NODE = new Element("TYPE_ALIAS_DECL_NODE", "type alias declaration");
    Element TYPE_DECL_NODE = new Element("TYPE_DECL_NODE", "type declaration");
    Element TYPE_NAME = new Element("TYPE_NAME", "name of a type");
    Element TYPE_NAME_REF = new Element("TYPE_NAME_REF", "name of a type");
    Element QUALIFIED_TYPE_NAME_REF = new Element("QUALIFIED_TYPE_NAME_REF", "qualified name of a type");
    Element RUNE_OF_AUTOCOMPLETION_EL = new Element("RUNE_OF_AUTOCOMPLETION_EL");
    Element PIPE_SEP = new Element("PIPE_SEP", "pipe separated list");
    Element COMMA_SEP = new Element("COMMA_SEP", "comma separated list");
    Element TYPE_DECL_DEF_NODE = new Element("TYPE_DECL_FED_NODE", "type definition");
    Element RECORD_TYPE = new Element("record type");
    Element SURROUND_CONTENTS = new Element("surround contents");
    Element TUPLE_TYPE = new Element("tuple type");

    Element TYPE_CONSTRUCTOR_ARGS = new Element("type constructor arguments");
    Element OPERATOR = new Element("operator");
    Element OPERATOR_SYMBOL = new Element("operator symbol");
    Element OPERATOR_SYMBOL_REF = new Element("operator symbol");

    Element INFIX_OPERATOR_DECLARATION = new Element("infix operator declaration");
    Element PORT_DECLARATION = new Element("port declaration");
    Element PORT_NAME = new Element("port name");

    class Factory {

        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == DECLARATIONS) {
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
            } else if (type == OPERATOR_SYMBOL) {
                return new OperatorSymbol(node);
            } else if (type == OPERATOR_SYMBOL_REF) {
                return new OperatorSymbolRef(node);
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
            } else if (type == VALUE_EXPORT) {
                return new ValueExport(node);
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
            } else if (type == TYPE_DECL_DEF_NODE) {
                return new TypeDeclDefNode(node);
            } else if (type == TYPE_NAME) {
                return new TypeName(node);
            } else if (type == TYPE_NAME_REF) {
                return new TypeNameRef(node);
            } else if (type == QUALIFIED_TYPE_NAME_REF) {
                return new QualifiedTypeNameRef(node);
            } else if (type == COMMA_SEP) {
                return new CommaSeparatedList(node);
            } else if (type == PIPE_SEP) {
                return new PipeSeparatedList(node);
            } else if (type == RECORD_TYPE) {
                return new RecordType(node);
            } else if (type == SURROUND_CONTENTS) {
                return new SurroundContents(node);
            } else if (type == TUPLE_TYPE) {
                return new TupleType(node);
            } else if (type == TYPE_CONSTRUCTOR_ARGS) {
                return new TypeConstructorArgs(node);
            } else if (type == INFIX_OPERATOR_DECLARATION) {
                return new OperatorDeclaration(node);
            } else if (type == PORT_DECLARATION) {
                return new PortDeclaration(node);
            } else if (type == PORT_NAME) {
                return new PortName(node);
            } else if (type == RUNE_OF_AUTOCOMPLETION_EL) {
                return new RuneOfAutocompletion(node);
            } else {
                throw new AssertionError("Unknown element type: " + type);
            }
        }
    }
}
