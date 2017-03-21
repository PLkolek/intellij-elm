package mkolaczek.elm.lexer;
import com.intellij.lexer.*;
import static mkolaczek.elm.psi.Tokens.*;
import java.util.LinkedList;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

%%

%{
   private final LinkedList<Integer> states = new LinkedList();

   private void yypushstate(int state) {
       states.addFirst(yystate());
       yybegin(state);
   }
   private void yypopstate() {
       final int state = states.removeFirst();
       yybegin(state);
   }

   private final LinkedList<Integer> indents = new LinkedList();
%}

%public
%class ElmLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{
    return;
%eof}


CLRF="\r"|"\n"|"\r\n"
LINE_WS=[\ \f\t]
WS={CLRF}|{LINE_WS}
DIGIT=[0-9]
CAP_VAR=[A-Z][a-zA-Z0-9ᛜ]*
LOW_VAR=[a-z][a-zA-Z0-9ᛜ]*
SYMBOL= [\+\-\/\*=\.<>:&\|\^\?%#~\!]
SYMBOL_OP={SYMBOL}({SYMBOL}|ᛜ)*
ESCAPE_CHAR=[abfnrtv\"\\\']
HEX=[a-fA-F0-9]
UNICODE_ESCAPE="\\"u{HEX}{4}
VALID_ESCAPE="\\"{ESCAPE_CHAR}
INVALID_ESCAPE="\\"[^abfnrtv\"\'\\\n']
INVALID_UNICODE_ESCAPE="\\u"[^ \"\']{0,4}
HEX_LITERAL="0x"{HEX}+
INVALID_HEX_LITERAL="0x"
EXPONENT_PART = [eE][\+\-]? {DIGIT}+
FRACTIONAL_PART = "."{DIGIT}+
INT=([1-9]{DIGIT}*) | "0"
FRACTIONAL_NUMBER = {INT} {FRACTIONAL_PART}? {EXPONENT_PART}?
NUMBER = {FRACTIONAL_NUMBER} | {HEX_LITERAL}

%state INCOMMENT
%state DOCCOMMENT
%state INLINECOMMENT
%state INMULTILINESTRING
%state INSTRING
%state INCHARACTER

%%
<YYINITIAL> {
  "port"                { return PORT; }
  "effect"              { return EFFECT; }
  "module"              { return MODULE; }
  "where"               { return WHERE; }
  "import"              { return IMPORT; }
  "as"                  { return AS; }
  "exposing"            { return EXPOSING; }
  "type"                { return TYPE; }
  "alias"               { return ALIAS; }
  "infixl"              { return INFIXL; }
  "infixr"              { return INFIXR; }
  "infix"               { return INFIX; }
  "let"                 { return LET; }
  "in"                  { return IN; }
  "::"                  { return CONS; }
  "("                   { return LPAREN; }
  ")"                   { return RPAREN; }
  "{"                   { return LBRACKET; }
  "}"                   { return RBRACKET; }
  "["                   { return LSQUAREBRACKET; }
  "]"                   { return RSQUAREBRACKET; }
  ","                   { return COMMA; }
  ".."                  { return OPEN_LISTING; }
  "="                   { return EQUALS; }
  "->"                  { return ARROW; }
  "--"                  { yypushstate(INLINECOMMENT); return LINE_COMMENT; }
  "|"                   { return PIPE; }
  "."                   { return DOT; }
  ":"                   { return COLON; }
  "_"                   { return UNDERSCORE; }
  "{-|"                 { yypushstate(DOCCOMMENT); return BEGIN_DOC_COMMENT;}
  "{-"                  { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
  "\"\"\""              { yypushstate(INMULTILINESTRING); return MULTILINE_STRING;}
  "\""                  { yypushstate(INSTRING); return QUOTE;}
  "\'"                  { yypushstate(INCHARACTER); return SINGLE_QUOTE;}
  "{-"                  { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
  {WS}+                 { return TokenType.WHITE_SPACE; }
  {CAP_VAR}             { return CAP_VAR; }
  {LOW_VAR}             { return LOW_VAR; }
  {SYMBOL_OP}           { return SYM_OP; }
  {INVALID_HEX_LITERAL} { return INVALID_HEX_NUMBER; }
  {DIGIT}               { return DIGIT; }
  {NUMBER}              { return NUMBER; }
  "ᛜ"                   { return RUNE_OF_AUTOCOMPLETION; }
  [^]                   { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<DOCCOMMENT> {
    "{-"            { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
    "-}"            { yypopstate(); return END_DOC_COMMENT; }
    {CLRF}          { return TokenType.WHITE_SPACE; }
    [^\-\}\{\r\n]+  { return COMMENT_CONTENT; }
    [\-\{\}]        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}


<INCOMMENT> {
    "{-"            { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
    "-}"            { yypopstate(); return END_COMMENT; }
    {CLRF}          { return TokenType.WHITE_SPACE; }
    [^\-\}\{\r\n]+  { return COMMENT_CONTENT; }
    [\-\{\}]        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INMULTILINESTRING> {
    "\"\"\""                    { yypopstate(); return MULTILINE_STRING; }
    {UNICODE_ESCAPE}            { return VALID_STRING_ESCAPE_TOKEN; }
    {VALID_ESCAPE}              { return VALID_STRING_ESCAPE_TOKEN; }
    {INVALID_ESCAPE}            { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    {INVALID_UNICODE_ESCAPE}    { return INVALID_UNICODE_ESCAPE_TOKEN; }
    [^\\\"]+                    { return STRING_CONTENT; }
    [\"\n]                      { return STRING_CONTENT; }
    "\\"                        { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    [^]                         { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INSTRING> {
    "\""                        { yypopstate(); return QUOTE; }
    {CLRF}                      { yypopstate(); return INVALID_EOL_IN_STRING; }
    {UNICODE_ESCAPE}            { return VALID_STRING_ESCAPE_TOKEN; }
    {VALID_ESCAPE}              { return VALID_STRING_ESCAPE_TOKEN; }
    {INVALID_ESCAPE}            { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    {INVALID_UNICODE_ESCAPE}    { return INVALID_UNICODE_ESCAPE_TOKEN; }
    [^\\\"\n]+                  { return STRING_CONTENT; }
    "\\"                        { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    [^]                         { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INCHARACTER> {
    "\'"                        { yypopstate(); return SINGLE_QUOTE; }
    {CLRF}                      { yypopstate(); return INVALID_EOL_IN_STRING; }
    {UNICODE_ESCAPE}            { return VALID_STRING_ESCAPE_TOKEN; }
    {VALID_ESCAPE}              { return VALID_STRING_ESCAPE_TOKEN; }
    {INVALID_ESCAPE}            { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    {INVALID_UNICODE_ESCAPE}    { return INVALID_UNICODE_ESCAPE_TOKEN; }
    [^\\\'\n]+                  { return STRING_CONTENT; }
    "\\"                        { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    [^]                         { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INLINECOMMENT> {
    {CLRF}          { yypopstate(); return TokenType.WHITE_SPACE; }
    [^\r\n]+        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}