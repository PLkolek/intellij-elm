package mkolaczek.elm;
import com.intellij.lexer.*;
import static mkolaczek.elm.psi.Tokens.*;
import java.util.LinkedList;
import com.intellij.psi.TokenType;

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
CAP_VAR=[A-Z][a-zA-Z0-9]*
LOW_VAR=[a-z][a-zA-Z0-9]*
SYMBOL= ! ( !( [+-/*=.$<>:&|\^?%#@~!,]
        | \p{General_Category:MathSymbol}
        | \p{General_Category:CurrencySymbol}
        | \p{General_Category:ModifierSymbol}
        | \p{General_Category:OtherSymbol}
        ) | "`" )

%state INCOMMENT
%state DOCCOMMENT
%state INLINECOMMENT

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
  "("               { return LPAREN; }
  ")"               { return RPAREN; }
  "{"               { return LBRACKET; }
  "}"               { return RBRACKET; }
  ","               { return COMMA; }
  ".."              { return OPEN_LISTING; }
  "="               { return EQUALS; }
  "->"              { return ARROW; }
  "--"              { yypushstate(INLINECOMMENT); return COMMENT; }
  "|"               { return OR; }
  "."               { return DOT; }
  ":"               { return COLON; }
  "{-|"             { yypushstate(DOCCOMMENT); return BEGIN_DOC_COMMENT;}
  "{-"              { yypushstate(INCOMMENT); return BEGIN_COMMENT; }
  {WS}+             { return TokenType.WHITE_SPACE; }
  {CAP_VAR}         { return CAP_VAR; }
  {LOW_VAR}         { return LOW_VAR; }
  ","+              { return COMMA_OP; }
  {SYMBOL}+         { return SYM_OP; }
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

<INLINECOMMENT> {
    {CLRF}          { yypopstate(); return TokenType.WHITE_SPACE; }
    [^\r\n]*        { return COMMENT_CONTENT; }
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}