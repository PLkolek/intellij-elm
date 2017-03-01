/* The following code was generated by JFlex 1.7.0-SNAPSHOT tweaked for IntelliJ platform */

package mkolaczek.elm;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.util.LinkedList;

import static mkolaczek.elm.psi.Tokens.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0-SNAPSHOT
 * from the specification file <tt>Elm.flex</tt>
 */
public class ElmLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int INCOMMENT = 2;
    public static final int DOCCOMMENT = 4;
    public static final int INLINECOMMENT = 6;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
          0, 0, 1, 1, 2, 2, 3, 3
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [11, 6, 4]
   * Total runtime size is 9536 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch>>10]<<6)|((ch>>4)&0x3f)]<<4)|(ch&0xf)];
  }

  /* The ZZ_CMAP_Z table has 1088 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\6\15\1\16\25\15"+
    "\1\17\1\20\23\15\1\21\1\22\1\23\1\15\1\24\2\15\1\25\24\15\1\26\24\15\1\27"+
    "\4\15\1\30\1\31\1\32\4\15\1\33\1\34\1\35\1\36\u03c1\15");

  /* The ZZ_CMAP_Y table has 1984 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\2\1\1\10\1\11\1\1\1\12\1\1\1\12\34\1\1\13"+
    "\1\14\1\15\1\16\7\1\1\17\1\20\6\1\1\21\10\1\1\22\17\1\1\23\7\1\1\24\14\1\1"+
    "\25\1\26\1\27\17\1\1\21\37\1\1\30\17\1\1\31\7\1\1\32\7\1\1\33\7\1\1\34\17"+
    "\1\1\26\13\1\1\34\14\1\1\35\1\36\1\1\1\37\7\1\1\40\1\41\1\42\13\1\1\40\57"+
    "\1\1\43\103\1\1\44\26\1\1\32\10\1\1\40\2\16\26\1\1\45\1\46\103\1\1\47\1\50"+
    "\2\23\1\27\4\1\1\51\1\22\1\1\2\52\1\1\1\16\1\53\4\1\1\54\1\55\1\56\1\57\1"+
    "\60\3\1\1\57\27\16\1\61\1\16\1\62\14\16\1\63\2\16\1\64\1\1\1\63\4\1\1\65\4"+
    "\16\1\43\1\1\46\16\1\66\2\1\1\67\2\16\1\70\1\16\1\71\31\16\1\72\1\73\3\16"+
    "\1\61\1\16\1\74\27\16\1\75\1\16\1\76\1\16\1\77\1\100\1\101\1\65\17\1\1\102"+
    "\31\1\1\16\1\103\5\16\1\104\15\16\1\71\1\1\1\105\1\51\1\106\1\32\1\107\5\1"+
    "\1\110\17\1\1\111\2\1\2\16\1\104\1\1\1\16\1\53\1\112\1\16\1\66\1\32\2\16\1"+
    "\112\2\16\1\32\3\16\1\53\20\16\134\1\4\16\51\1\3\16\1\64\43\1\1\16\1\64\1"+
    "\101\5\1\1\113\11\1\1\114\1\115\43\1\1\116\15\1\1\44\74\1\1\26\10\1\1\14\1"+
    "\101\42\1\1\117\6\1\1\120\11\1\1\121\1\122\1\1\1\25\1\32\1\123\10\1\1\124"+
    "\1\117\23\1\1\125\3\1\1\73\1\126\1\105\1\32\2\1\2\16\1\127\47\1\1\130\44\1"+
    "\1\131\106\1\1\34\77\1\1\65\1\17\24\1\1\132\66\1\17\16\1\71\2\16\1\133\3\16"+
    "\1\134\1\1\1\135\1\16\1\136\3\16\1\137\1\1\4\16\1\140\13\1\5\16\1\64\66\1"+
    "\1\31\1\44\1\1\1\44\1\1\1\17\1\1\1\17\1\34\1\1\1\34\1\1\1\26\1\1\1\26\1\1"+
    "\1\141\3\1\40\16\3\1\1\142\2\1\1\23\1\143\1\144\106\1\1\101\20\1\2\16\1\105"+
    "\6\16\1\104\1\53\3\145\1\16\1\71\1\1\1\16\1\53\3\16\1\105\2\16\1\63\4\1\1"+
    "\146\1\16\1\72\2\16\1\63\1\137\1\101\12\1\47\16\1\103\2\16\1\147\22\16\1\32"+
    "\1\127\1\104\7\16\1\104\5\16\1\150\2\1\1\105\3\16\1\66\1\43\2\16\1\66\1\16"+
    "\1\151\6\1\1\137\6\1\1\150\3\1\1\32\43\1");

  /* The ZZ_CMAP_A table has 1696 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\3\1\2\1\0\1\3\1\1\22\0\1\3\1\7\1\0\4\7\1\0\1\34\1\35\2\7\1\40\1\43"+
    "\1\41\1\7\12\5\1\46\1\0\1\7\1\42\1\44\2\7\32\4\3\0\1\7\2\0\1\26\1\6\1\16\1"+
    "\20\1\14\1\15\1\32\1\24\1\25\2\6\1\22\1\17\1\31\1\11\1\10\1\6\1\12\1\27\1"+
    "\13\1\21\1\6\1\23\1\30\1\33\1\6\1\36\1\45\1\37\1\7\3\0\5\7\1\0\2\7\2\0\1\7"+
    "\1\0\4\7\2\0\1\7\3\0\1\7\16\0\1\7\12\0\4\7\14\0\16\7\5\0\7\7\1\0\1\7\1\0\21"+
    "\7\5\0\1\7\16\0\2\7\20\0\1\7\13\0\1\7\32\0\3\7\6\0\3\7\2\0\1\7\2\0\2\7\16"+
    "\0\1\7\12\0\1\7\23\0\2\7\3\0\2\7\6\0\2\7\5\0\1\7\16\0\1\7\22\0\10\7\24\0\1"+
    "\7\1\0\3\7\17\0\1\7\1\0\3\7\2\0\6\7\4\0\1\7\1\0\1\7\1\0\1\7\25\0\10\7\1\0"+
    "\6\7\1\0\2\7\5\0\4\7\7\0\12\7\21\0\1\7\5\0\12\7\11\0\11\7\20\0\1\7\1\0\3\7"+
    "\13\0\3\7\4\0\1\7\25\0\3\7\3\0\17\7\1\0\2\7\1\0\4\7\1\0\2\7\12\0\1\7\1\0\3"+
    "\7\5\0\6\7\1\0\1\7\1\0\1\7\1\0\1\7\4\0\1\7\13\0\2\7\4\0\5\7\5\0\4\7\1\0\11"+
    "\7\4\0\15\7\2\0\20\7\5\0\7\7\25\0\14\7\14\0\21\7\2\0\17\7\12\0\3\7\26\0\23"+
    "\7\2\0\6\7\2\0\20\7\2\0\22\7\3\0\14\7\1\0\10\7\23\0\6\7\5\0\12\7\1\0\11\7"+
    "\14\0\14\7\6\0\2\7\22\0\2\7\6\0\2\7\13\0\2\7\3\0\2\7\4\0\12\7\12\0\6\7\11"+
    "\0\2\7\15\0\4\7\12\0\4\7\15\0\3\7\22\0\2\7\4\0\1\7\1\0\3\7\2\0\1\7\12\0\1"+
    "\7\6\0\1\7\20\0\3\7\15\0\1\7\1\0\1\7\1\0\7\7\1\0\7\7\10\0\23\7\2\0\1\7\3\0"+
    "\15\7\12\0\2\7\17\0\1\7\23\0\1\7\3\0\7\7\2\0\14\7\5\0\3\7\6\0\2\7\7\0\16\7"+
    "\4\0\13\7\7\0\2\7\3\0\1\7\15\0\1\7\23\0\4\7\5\0\5\7\1\0\16\7\1\0\2\7\12\0"+
    "\17\7\6\0\16\7\1\0\20\7\13\0\16\7\2\0");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
          "\1\0\3\1\1\2\1\3\1\4\1\5\1\6\7\5" +
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\6"+
                  "\1\16\1\17\1\1\3\3\5\1\3\20\10\5\1\21" +
                  "\1\22\1\23\1\24\1\25\1\26\1\22\1\27\1\30" +
                  "\10\5\1\31\1\32\1\33\11\5\1\34\1\5\1\35" +
                  "\1\36\1\5\1\37\1\40\1\5\1\41";

  private static int [] zzUnpackAction() {
      int[] result = new int[84];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\47\0\116\0\165\0\234\0\303\0\352\0\u0111"+
            "\0\u0138\0\u015f\0\u0186\0\u01ad\0\u01d4\0\u01fb\0\u0222\0\u0249" +
            "\0\234\0\234\0\u0270\0\234\0\u0297\0\u02be\0\u0138\0\u02e5" +
            "\0\u0138\0\u0138\0\u030c\0\u0333\0\234\0\u030c\0\u035a\0\234" +
            "\0\u0381\0\u03a8\0\u03cf\0\u03f6\0\234\0\u03cf\0\u041d\0\u0444" +
            "\0\u046b\0\u0492\0\u04b9\0\u04e0\0\u0507\0\u052e\0\u0111\0\u0555" +
            "\0\u0297\0\u0138\0\u0138\0\u0138\0\234\0\234\0\234\0\u057c" +
            "\0\u05a3\0\u05ca\0\u05f1\0\u0618\0\u063f\0\u0666\0\u068d\0\234" +
            "\0\u0111\0\u0111\0\u06b4\0\u06db\0\u0702\0\u0729\0\u0750\0\u0777" +
            "\0\u079e\0\u07c5\0\u07ec\0\u0111\0\u0813\0\u0111\0\u0111\0\u083a" +
            "\0\u0111\0\u0111\0\u0861\0\u0111";

  private static int [] zzUnpackRowMap() {
      int[] result = new int[84];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
          "\1\5\3\6\1\7\1\5\1\10\1\11\1\12\2\10" +
                  "\1\13\1\14\2\10\1\15\3\10\1\16\1\10\1\17" +
                  "\1\20\5\10\1\21\1\22\1\23\1\24\1\25\1\26" +
                  "\1\27\1\30\1\11\1\31\1\32\1\33\1\34\1\35" +
                  "\1\36\32\33\1\37\1\40\3\33\1\41\4\33\1\34" +
                  "\1\35\1\36\32\33\1\37\1\40\3\33\1\42\3\33" +
                  "\1\43\1\44\1\45\1\46\43\43\50\0\3\6\47\0" +
                  "\3\7\1\0\24\7\17\0\3\10\1\0\24\10\22\0" +
                  "\1\11\30\0\7\11\4\0\3\10\1\0\1\10\1\47" +
                  "\22\10\17\0\3\10\1\0\23\10\1\50\17\0\3\10" +
                  "\1\0\5\10\1\51\12\10\1\52\3\10\17\0\3\10" +
                  "\1\0\1\10\1\53\22\10\17\0\3\10\1\0\14\10" +
                  "\1\54\7\10\17\0\3\10\1\0\7\10\1\55\14\10" +
                  "\17\0\3\10\1\0\12\10\1\56\4\10\1\57\4\10" +
                  "\56\0\1\60\12\0\1\11\30\0\1\61\6\11\7\0" +
                  "\1\11\30\0\1\11\1\62\5\11\7\0\1\11\30\0" +
                  "\3\11\1\63\1\64\2\11\1\33\2\0\33\33\2\0" +
                  "\3\33\1\0\3\33\2\0\1\35\107\0\1\65\42\0" +
                  "\1\66\46\0\1\67\7\0\1\43\2\0\44\43\2\0" +
                  "\1\45\50\0\3\10\1\0\2\10\1\70\21\10\17\0" +
                  "\3\10\1\0\1\71\23\10\17\0\3\10\1\0\5\10" +
                  "\1\72\16\10\17\0\3\10\1\0\1\73\23\10\17\0" +
                  "\3\10\1\0\10\10\1\74\13\10\17\0\3\10\1\0" +
                  "\4\10\1\75\17\10\17\0\3\10\1\0\1\76\23\10" +
                  "\17\0\3\10\1\0\15\10\1\77\6\10\60\0\1\100" +
                  "\5\0\3\10\1\0\3\10\1\101\20\10\17\0\3\10" +
                  "\1\0\4\10\1\102\17\10\17\0\3\10\1\0\4\10" +
                  "\1\103\17\10\17\0\3\10\1\0\1\10\1\104\22\10" +
                  "\17\0\3\10\1\0\11\10\1\105\12\10\17\0\3\10" +
                  "\1\0\2\10\1\106\21\10\17\0\3\10\1\0\1\10" +
                  "\1\107\22\10\17\0\3\10\1\0\16\10\1\110\5\10" +
                  "\17\0\3\10\1\0\6\10\1\111\15\10\17\0\3\10" +
                  "\1\0\17\10\1\112\4\10\17\0\3\10\1\0\12\10" +
                  "\1\113\11\10\17\0\3\10\1\0\4\10\1\114\17\10" +
                  "\17\0\3\10\1\0\2\10\1\115\21\10\17\0\3\10" +
                  "\1\0\17\10\1\116\4\10\17\0\3\10\1\0\3\10" +
                  "\1\117\20\10\17\0\3\10\1\0\15\10\1\120\6\10" +
                  "\17\0\3\10\1\0\4\10\1\121\17\10\17\0\3\10" +
                  "\1\0\3\10\1\122\20\10\17\0\3\10\1\0\21\10" +
                  "\1\123\2\10\17\0\3\10\1\0\22\10\1\124\1\10" +
                  "\13\0";

  private static int [] zzUnpackTrans() {
      int[] result = new int[2184];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
          "\1\0\3\1\1\11\13\1\2\11\1\1\1\11\10\1" +
                  "\1\11\2\1\1\11\4\1\1\11\17\1\3\11\10\1" +
    "\1\11\24\1";

  private static int [] zzUnpackAttribute() {
      int[] result = new int[84];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
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


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public ElmLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
        return;

    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        zzDoEOF();
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return COMMENT_CONTENT;
            }
            case 34:
                break;
          case 2: 
            { return com.intellij.psi.TokenType.BAD_CHARACTER;
            }
            case 35:
                break;
          case 3: 
            { return TokenType.WHITE_SPACE;
            }
            case 36:
                break;
          case 4: 
            { return CAP_VAR;
            }
            case 37:
                break;
          case 5: 
            { return LOW_VAR;
            }
            case 38:
                break;
          case 6: 
            { return SYM_OP;
            }
            case 39:
                break;
          case 7: 
            { return LPAREN;
            }
            case 40:
                break;
          case 8: 
            { return RPAREN;
            }
            case 41:
                break;
          case 9: 
            { return LBRACKET;
            }
            case 42:
                break;
          case 10: 
            { return RBRACKET;
            }
            case 43:
                break;
          case 11: 
            { return COMMA;
            }
            case 44:
                break;
          case 12: 
            { return DOT;
            }
            case 45:
                break;
          case 13: 
            { return EQUALS;
            }
            case 46:
                break;
          case 14: {
            return PIPE;
            }
            case 47:
                break;
          case 15: 
            { return COLON;
            }
            case 48:
                break;
          case 16: 
            { yypopstate(); return TokenType.WHITE_SPACE;
            }
            case 49:
                break;
          case 17: 
            { return AS;
            }
            case 50:
                break;
          case 18: 
            { yypushstate(INCOMMENT); return BEGIN_COMMENT;
            }
            case 51:
                break;
          case 19: 
            { return COMMA_OP;
            }
            case 52:
                break;
          case 20: 
            { return OPEN_LISTING;
            }
            case 53:
                break;
          case 21: {
            yypushstate(INLINECOMMENT);
            return LINE_COMMENT;
            }
            case 54:
                break;
          case 22: 
            { return ARROW;
            }
            case 55:
                break;
          case 23: 
            { yypopstate(); return END_COMMENT;
            }
          case 56: break;
            case 24: {
                yypopstate();
                return END_DOC_COMMENT;
            }
          case 57: break;
            case 25: {
                yypushstate(DOCCOMMENT);
                return BEGIN_DOC_COMMENT;
            }
          case 58: break;
            case 26: {
                return PORT;
            }
          case 59: break;
            case 27: {
                return TYPE;
            }
          case 60: break;
            case 28: {
                return WHERE;
            }
          case 61: break;
            case 29: {
                return ALIAS;
            }
          case 62: break;
            case 30: {
                return EFFECT;
            }
          case 63: break;
            case 31: {
                return MODULE;
            }
            case 64:
                break;
            case 32: {
                return IMPORT;
            }
            case 65:
                break;
            case 33:
            { return EXPOSING;
            }
            case 66:
                break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
