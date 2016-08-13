package mkolaczek.elm;
import com.intellij.lexer.*;
import com.intellij.mkolaczek.elm.psi.tree.IElementType;
import static mkolaczek.elm.psi.ElmTypes.*;
import com.intellij.mkolaczek.elm.psi.TokenType;
import java.util.LinkedList;

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
LINE_WS=[\ \f]
CAP_VAR=[A-Z][a-zA-Z0-9]*
LOW_VAR=[a-z][a-zA-Z0-9]*
FRESH_LINE=({LINE_WS}|{CLRF})*{CLRF}
FORCED_WS=({LINE_WS}|{CLRF})*{LINE_WS}
SYMBOL= ! ( !( [+-/*=.$<>:&|\^?%#@~!,]
        | [:unicode_math_symbol:]
        | [:unicode_currency_symbol:]
        | [:unicode_modifier_symbol:]
        | [:unicode_other_symbol:]
        ) | "`" )

%state INCOMMENT
%state INDENT

%%
<YYINITIAL> {
  "module"          { return MODULE; }
  "where"           { return WHERE; }
  "import"          { return IMPORT; }
  "as"              { return AS; }
  "exposing"        { return EXPOSING; }
  "type"            { return TYPE; }
  "alias"           { return ALIAS; }
  "("               { return LPAREN; }
  ")"               { return RPAREN; }
  ","               { return COMMA; }
  ".."              { return OPEN_LISTING; }
  "="               { return EQUALS; }
  "->"              { return ARROW; }
  "--"              { return COMMENT; }
  "|"               { return OR; }
  "."               { return DOT; }
  ":"               { return COLON; }
  "{-|"             { yypushstate(INCOMMENT); return BEGIN_DOC_COMMENT;}
  {LINE_WS}         { return WHITE_SPACE; }
  {CLRF}            { return NEW_LINE; }
  {CAP_VAR}         { return CAP_VAR; }
  {LOW_VAR}         { return LOW_VAR; }
  ","+              { return COMMA_OP; }
  {SYMBOL}+         { return SYM_OP; }
  [^]               { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

<INCOMMENT> {
    "{-"            { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
    "-}"            { yypopstate(); return END_COMMENT; }
    [^\-\}\{]*      { return COMMENT_CONTENT; }
    [\-\{\}]        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}