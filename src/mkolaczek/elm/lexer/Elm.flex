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


CLRF="\r"|"\n"|"\r\n"|[\ \f\t]
LINE_WS=[\ \f\t]
WS={CLRF}|{LINE_WS}
DIGIT=[0-9]
CAP_VAR=[A-Z][a-zA-Z0-9ᛜ]*
LOW_VAR=[a-z][a-zA-Z0-9ᛜ]*
SYMBOL= [\+\-\/\*=\.<>:&\|\^\?%#~\!]
SYMBOL_OP={SYMBOL}({SYMBOL}|ᛜ)*

%state INCOMMENT
%state DOCCOMMENT
%state INLINECOMMENT
%state INMULTILINESTRING
%state INSTRING

%%
<YYINITIAL> {
  "port"            { return PORT; }
  "effect"          { return EFFECT; }
  "module"          { return MODULE; }
  "where"           { return WHERE; }
  "import"          { return IMPORT; }
  "as"              { return AS; }
  "exposing"        { return EXPOSING; }
  "type"            { return TYPE; }
  "alias"           { return ALIAS; }
  "infixl"          { return INFIXL; }
  "infixr"          { return INFIXR; }
  "infix"           { return INFIX; }
  "::"              { return CONS; }
  "("               { return LPAREN; }
  ")"               { return RPAREN; }
  "{"               { return LBRACKET; }
  "}"               { return RBRACKET; }
  "["               { return LSQUAREBRACKET; }
  "]"               { return RSQUAREBRACKET; }
  ","               { return COMMA; }
  ".."              { return OPEN_LISTING; }
  "="               { return EQUALS; }
  "->"              { return ARROW; }
  "--"              { yypushstate(INLINECOMMENT); return LINE_COMMENT; }
  "|"               { return PIPE; }
  "."               { return DOT; }
  ":"               { return COLON; }
  "_"               { return UNDERSCORE; }
  "{-|"             { yypushstate(DOCCOMMENT); return BEGIN_DOC_COMMENT;}
  "{-"              { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
  "\"\"\""          { yypushstate(INMULTILINESTRING); return MULTILINE_STRING;}
  "\""              { yypushstate(INSTRING); return QUOTE;}
  "{-"              { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
  {WS}+             { return TokenType.WHITE_SPACE; }
  {CAP_VAR}         { return CAP_VAR; }
  {LOW_VAR}         { return LOW_VAR; }
  {SYMBOL_OP}       { return SYM_OP; }
  {DIGIT}           { return DIGIT; }
  "ᛜ"               { return RUNE_OF_AUTOCOMPLETION; }
  [^]               { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<DOCCOMMENT> {
    "{-"            { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
    "-}"            { yypopstate(); return END_DOC_COMMENT; }
    {CLRF}          { return TokenType.WHITE_SPACE; }
    [^\-\}\{\r\n]*  { return COMMENT_CONTENT; }
    [\-\{\}]        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}


<INCOMMENT> {
    "{-"            { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
    "-}"            { yypopstate(); return END_COMMENT; }
    {CLRF}          { return TokenType.WHITE_SPACE; }
    [^\-\}\{\r\n]*  { return COMMENT_CONTENT; }
    [\-\{\}]        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INMULTILINESTRING> {
    "\"\"\""        { yypopstate(); return MULTILINE_STRING; }
    [^\"]*          { return STRING_CONTENT; }
    [\"]            { return STRING_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INSTRING> {
    "\\\""          { return STRING_CONTENT; }
    "\""            { yypopstate(); return QUOTE; }
    {CLRF}          { yypopstate(); return TokenType.WHITE_SPACE; }
    [^\"\n]*        { return STRING_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INLINECOMMENT> {
    {CLRF}          { yypopstate(); return TokenType.WHITE_SPACE; }
    [^\r\n]*        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}