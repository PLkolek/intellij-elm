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
    Element TYPE_DECLARATION = new Element("type declaration");
    Element MODULE_NAME = new Element("module name");
    Element MODULE_NAME_REF = new Element("MODULE_NAME_REF", "module name");
    Element MODULE_ALIAS = new Element("module alias");
    Element MODULE_NODE = new Element("MODULE_NODE", "module");
    Element EXPOSED_VALUE = new Element("EXPOSED_VALUE", "exposed value");
    Element EXPOSING_NODE = new Element("EXPOSING_NODE", "exposing");
    Element EFFECT_MODULE_SETTINGS = new Element("EFFECT_MODULE_SETTINGS", "module settings");
    Element EFFECT_MODULE_SETTINGS_LIST = new Element("EFFECT_MODULE_SETTINGS_LIST", "module settings list");
    Element EFFECT_MODULE_SETTING = new Element("EFFECT_MODULE_SETTING", "module setting");
    Element OPEN_LISTING_NODE = new Element("OPEN_LISTING_NODE", "open listing");
    Element IMPORTS = new Element("imports");
    Element TYPE_EXPOSING = new Element("TYPE_EXPOSING", "exposed type");
    Element VALUE_EXPOSING = new Element("VALUE_EXPOSING", "exposed value");
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
    Element SURROUND_CONTENTS = new Element("surround contents");

    Element TYPE_CONSTRUCTOR_ARGS = new Element("type constructor arguments");
    Element OPERATOR = new Element("operator");
    Element OPERATOR_SYMBOL = new Element("operator symbol");
    Element OPERATOR_SYMBOL_REF = new Element("operator symbol");

    Element INFIX_OPERATOR_DECLARATION = new Element("infix operator declaration");
    Element PORT_DECLARATION = new Element("port declaration");
    Element VALUE_DECLARATION = new Element("value declaration");

    Element PORT_NAME = new Element("port name");
    Element STRING_LITERAL = new Element("string literal");


    Element CHARACTER_LITERAL = new Element("character literal");
    Element EXPRESSION = new Element("expression");
    Element OPERAND = new Element("operand");
    Element LET_EXPRESSION = new Element("let expression");
    Element IF_EXPRESSION = new Element("if expression");
    Element CASE_EXPRESSION = new Element("case expression");
    Element VALUE_NAME = new Element("pattern variable");
    Element PATTERN_TERM = new Element("pattern term");
    Element DEFINED_VALUES = new Element("defined values");
    Element QUALIFIED_TYPE_CONSTRUCTOR_REF = new Element("quallified reference");
    Element VAR = new Element("variable name");
    Element QUALIFIED_VAR = new Element("qualified var");
    Element CASE_BRANCH = new Element("case branch");

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
            } else if (type == EXPOSED_VALUE) {
                return new ExposedValue(node);
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
            } else if (type == TYPE_EXPOSING) {
                return new TypeExposing(node);
            } else if (type == VALUE_EXPOSING) {
                return new ValueExposing(node);
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
            } else if (type == QUALIFIED_TYPE_CONSTRUCTOR_REF) {
                return new QualifiedTypeConstructorRef(node);
            } else if (type == COMMA_SEP) {
                return new CommaSeparatedList(node);
            } else if (type == PIPE_SEP) {
                return new PipeSeparatedList(node);
            } else if (type == SURROUND_CONTENTS) {
                return new SurroundContents(node);
            } else if (type == TYPE_CONSTRUCTOR_ARGS) {
                return new TypeConstructorArgs(node);
            } else if (type == INFIX_OPERATOR_DECLARATION) {
                return new OperatorDeclaration(node);
            } else if (type == VALUE_DECLARATION) {
                return new ValueDeclaration(node);
            } else if (type == PORT_DECLARATION) {
                return new PortDeclaration(node);
            } else if (type == PORT_NAME) {
                return new PortName(node);
            } else if (type == STRING_LITERAL) {
                return new StringLiteral(node);
            } else if (type == CHARACTER_LITERAL) {
                return new CharacterLiteral(node);
            } else if (type == EXPRESSION) {
                return new Expression(node);
            } else if (type == LET_EXPRESSION) {
                return new LetExpression(node);
            } else if (type == IF_EXPRESSION) {
                return new IfExpression(node);
            } else if (type == CASE_EXPRESSION) {
                return new CaseExpression(node);
            } else if (type == OPERAND) {
                return new Operand(node);
            } else if (type == VALUE_NAME) {
                return new ValueName(node);
            } else if (type == PATTERN_TERM) {
                return new PatternTerm(node);
            } else if (type == DEFINED_VALUES) {
                return new DefinedValues(node);
            } else if (type == VAR) {
                return new Var(node);
            } else if (type == QUALIFIED_VAR) {
                return new QualifiedVar(node);
            } else if (type == CASE_BRANCH) {
                return new CaseBranch(node);
            } else if (type == RUNE_OF_AUTOCOMPLETION_EL) {
                return new RuneOfAutocompletion(node);
            } else {
                throw new AssertionError("Unknown element type: " + type);
            }
        }
    }
}
