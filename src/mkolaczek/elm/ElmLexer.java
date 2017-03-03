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
   * Total runtime size is 9568 bytes
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
            "\1\1\43\63\1\1\44\17\1\1\45\26\1\1\32\10\1\1\40\2\16\26\1\1\46\1\47\103\1" +
            "\1\50\1\51\2\23\1\27\4\1\1\52\1\22\1\1\2\53\1\1\1\16\1\54\4\1\1\55\1\56\1" +
            "\57\1\60\1\61\3\1\1\60\27\16\1\62\1\16\1\63\14\16\1\64\2\16\1\65\1\1\1\64" +
            "\4\1\1\66\4\16\1\43\1\1\46\16\1\67\2\1\1\70\2\16\1\71\1\16\1\72\31\16\1\73" +
            "\1\74\3\16\1\62\1\16\1\75\27\16\1\76\1\16\1\77\1\16\1\100\1\101\1\102\1\66" +
            "\17\1\1\103\31\1\1\16\1\104\5\16\1\105\15\16\1\72\1\1\1\106\1\52\1\107\1\32" +
            "\1\110\5\1\1\111\17\1\1\112\2\1\2\16\1\105\1\1\1\16\1\54\1\113\1\16\1\67\1" +
            "\32\2\16\1\113\2\16\1\32\3\16\1\54\20\16\134\1\4\16\51\1\3\16\1\65\43\1\1" +
            "\16\1\65\1\102\5\1\1\114\11\1\1\115\1\116\43\1\1\117\15\1\1\45\74\1\1\26\10" +
            "\1\1\14\1\102\42\1\1\120\6\1\1\121\11\1\1\122\1\123\1\1\1\25\1\32\1\124\10" +
            "\1\1\125\1\120\23\1\1\126\3\1\1\74\1\127\1\106\1\32\2\1\2\16\1\130\47\1\1" +
            "\131\44\1\1\132\106\1\1\34\77\1\1\66\1\17\24\1\1\133\66\1\17\16\1\72\2\16" +
            "\1\134\3\16\1\135\1\1\1\136\1\16\1\137\3\16\1\140\1\1\4\16\1\141\13\1\5\16" +
            "\1\65\66\1\1\31\1\45\1\1\1\45\1\1\1\17\1\1\1\17\1\34\1\1\1\34\1\1\1\26\1\1" +
            "\1\26\1\1\1\142\3\1\40\16\3\1\1\143\2\1\1\23\1\144\1\145\106\1\1\102\20\1" +
            "\2\16\1\106\6\16\1\105\1\54\3\146\1\16\1\72\1\1\1\16\1\54\3\16\1\106\2\16" +
            "\1\64\4\1\1\147\1\16\1\73\2\16\1\64\1\140\1\102\12\1\47\16\1\104\2\16\1\150" +
            "\22\16\1\32\1\130\1\105\7\16\1\105\5\16\1\151\2\1\1\106\3\16\1\67\1\43\2\16" +
            "\1\67\1\16\1\152\6\1\1\140\6\1\1\151\3\1\1\32\43\1");

  /* The ZZ_CMAP_A table has 1712 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\3\1\2\1\0\1\3\1\1\22\0\1\3\1\7\1\0\4\7\1\0\1\34\1\35\2\7\1\40\1\43"+
    "\1\41\1\7\12\5\1\46\1\0\1\7\1\42\1\44\2\7\32\4\3\0\1\7\2\0\1\26\1\6\1\16\1"+
    "\20\1\14\1\15\1\32\1\24\1\25\2\6\1\22\1\17\1\31\1\11\1\10\1\6\1\12\1\27\1"+
    "\13\1\21\1\6\1\23\1\30\1\33\1\6\1\36\1\45\1\37\1\7\3\0\5\7\1\0\2\7\2\0\1\7"+
    "\1\0\4\7\2\0\1\7\3\0\1\7\16\0\1\7\12\0\4\7\14\0\16\7\5\0\7\7\1\0\1\7\1\0\21"+
    "\7\5\0\1\7\16\0\2\7\20\0\1\7\13\0\1\7\32\0\3\7\6\0\3\7\2\0\1\7\2\0\2\7\16"+
    "\0\1\7\12\0\1\7\23\0\2\7\3\0\2\7\6\0\2\7\5\0\1\7\16\0\1\7\22\0\10\7\24\0\1"+
    "\7\1\0\3\7\17\0\1\7\1\0\3\7\2\0\6\7\4\0\1\7\1\0\1\7\1\0\1\7\25\0\10\7\1\0"+
            "\6\7\1\0\2\7\5\0\4\7\7\0\12\7\22\0\1\47\16\0\1\7\5\0\12\7\11\0\11\7\20\0\1" +
            "\7\1\0\3\7\13\0\3\7\4\0\1\7\25\0\3\7\3\0\17\7\1\0\2\7\1\0\4\7\1\0\2\7\12\0" +
            "\1\7\1\0\3\7\5\0\6\7\1\0\1\7\1\0\1\7\1\0\1\7\4\0\1\7\13\0\2\7\4\0\5\7\5\0" +
            "\4\7\1\0\11\7\4\0\15\7\2\0\20\7\5\0\7\7\25\0\14\7\14\0\21\7\2\0\17\7\12\0" +
            "\3\7\26\0\23\7\2\0\6\7\2\0\20\7\2\0\22\7\3\0\14\7\1\0\10\7\23\0\6\7\5\0\12" +
            "\7\1\0\11\7\14\0\14\7\6\0\2\7\22\0\2\7\6\0\2\7\13\0\2\7\3\0\2\7\4\0\12\7\12" +
            "\0\6\7\11\0\2\7\15\0\4\7\12\0\4\7\15\0\3\7\22\0\2\7\4\0\1\7\1\0\3\7\2\0\1" +
            "\7\12\0\1\7\6\0\1\7\20\0\3\7\15\0\1\7\1\0\1\7\1\0\7\7\1\0\7\7\10\0\23\7\2" +
            "\0\1\7\3\0\15\7\12\0\2\7\17\0\1\7\23\0\1\7\3\0\7\7\2\0\14\7\5\0\3\7\6\0\2" +
            "\7\7\0\16\7\4\0\13\7\7\0\2\7\3\0\1\7\15\0\1\7\23\0\4\7\5\0\5\7\1\0\16\7\1" +
            "\0\2\7\12\0\17\7\6\0\16\7\1\0\20\7\13\0\16\7\2\0");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
          "\1\0\3\1\1\2\1\3\1\4\1\5\1\6\7\5" +
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\6"+
                  "\1\16\1\17\1\20\1\1\3\3\5\1\3\21\10\5" +
                  "\1\22\1\23\1\24\1\25\1\26\1\27\1\23\1\30" +
                  "\1\31\10\5\1\32\1\33\1\34\11\5\1\35\1\5" +
                  "\1\36\1\37\1\5\1\40\1\41\1\5\1\42";

  private static int [] zzUnpackAction() {
    int[] result = new int[85];
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
          "\0\0\0\50\0\120\0\170\0\240\0\310\0\360\0\u0118" +
                  "\0\u0140\0\u0168\0\u0190\0\u01b8\0\u01e0\0\u0208\0\u0230\0\u0258" +
                  "\0\240\0\240\0\u0280\0\240\0\u02a8\0\u02d0\0\u0140\0\u02f8" +
                  "\0\u0140\0\u0140\0\240\0\u0320\0\u0348\0\240\0\u0320\0\u0370" +
                  "\0\240\0\u0398\0\u03c0\0\u03e8\0\u0410\0\240\0\u03e8\0\u0438" +
                  "\0\u0460\0\u0488\0\u04b0\0\u04d8\0\u0500\0\u0528\0\u0550\0\u0118" +
                  "\0\u0578\0\u02a8\0\u0140\0\u0140\0\u0140\0\240\0\240\0\240" +
                  "\0\u05a0\0\u05c8\0\u05f0\0\u0618\0\u0640\0\u0668\0\u0690\0\u06b8" +
                  "\0\240\0\u0118\0\u0118\0\u06e0\0\u0708\0\u0730\0\u0758\0\u0780" +
                  "\0\u07a8\0\u07d0\0\u07f8\0\u0820\0\u0118\0\u0848\0\u0118\0\u0118" +
                  "\0\u0870\0\u0118\0\u0118\0\u0898\0\u0118";

  private static int [] zzUnpackRowMap() {
    int[] result = new int[85];
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
                  "\1\36\1\37\32\34\1\40\1\41\3\34\1\42\5\34" +
                  "\1\35\1\36\1\37\32\34\1\40\1\41\3\34\1\43" +
                  "\4\34\1\44\1\45\1\46\1\47\44\44\51\0\3\6" +
                  "\50\0\3\7\1\0\24\7\13\0\1\7\4\0\3\10" +
                  "\1\0\24\10\13\0\1\10\7\0\1\11\30\0\7\11" +
                  "\5\0\3\10\1\0\1\10\1\50\22\10\13\0\1\10" +
                  "\4\0\3\10\1\0\23\10\1\51\13\0\1\10\4\0" +
                  "\3\10\1\0\5\10\1\52\12\10\1\53\3\10\13\0" +
                  "\1\10\4\0\3\10\1\0\1\10\1\54\22\10\13\0" +
                  "\1\10\4\0\3\10\1\0\14\10\1\55\7\10\13\0" +
                  "\1\10\4\0\3\10\1\0\7\10\1\56\14\10\13\0" +
                  "\1\10\4\0\3\10\1\0\12\10\1\57\4\10\1\60" +
                  "\4\10\13\0\1\10\43\0\1\61\13\0\1\11\30\0" +
                  "\1\62\6\11\10\0\1\11\30\0\1\11\1\63\5\11" +
                  "\10\0\1\11\30\0\3\11\1\64\1\65\2\11\1\0" +
                  "\1\34\2\0\33\34\2\0\3\34\1\0\4\34\2\0" +
                  "\1\36\110\0\1\66\43\0\1\67\47\0\1\70\10\0" +
                  "\1\44\2\0\45\44\2\0\1\46\51\0\3\10\1\0" +
                  "\2\10\1\71\21\10\13\0\1\10\4\0\3\10\1\0" +
                  "\1\72\23\10\13\0\1\10\4\0\3\10\1\0\5\10" +
                  "\1\73\16\10\13\0\1\10\4\0\3\10\1\0\1\74" +
                  "\23\10\13\0\1\10\4\0\3\10\1\0\10\10\1\75" +
                  "\13\10\13\0\1\10\4\0\3\10\1\0\4\10\1\76" +
                  "\17\10\13\0\1\10\4\0\3\10\1\0\1\77\23\10" +
                  "\13\0\1\10\4\0\3\10\1\0\15\10\1\100\6\10" +
                  "\13\0\1\10\45\0\1\101\6\0\3\10\1\0\3\10" +
                  "\1\102\20\10\13\0\1\10\4\0\3\10\1\0\4\10" +
                  "\1\103\17\10\13\0\1\10\4\0\3\10\1\0\4\10" +
                  "\1\104\17\10\13\0\1\10\4\0\3\10\1\0\1\10" +
                  "\1\105\22\10\13\0\1\10\4\0\3\10\1\0\11\10" +
                  "\1\106\12\10\13\0\1\10\4\0\3\10\1\0\2\10" +
                  "\1\107\21\10\13\0\1\10\4\0\3\10\1\0\1\10" +
                  "\1\110\22\10\13\0\1\10\4\0\3\10\1\0\16\10" +
                  "\1\111\5\10\13\0\1\10\4\0\3\10\1\0\6\10" +
                  "\1\112\15\10\13\0\1\10\4\0\3\10\1\0\17\10" +
                  "\1\113\4\10\13\0\1\10\4\0\3\10\1\0\12\10" +
                  "\1\114\11\10\13\0\1\10\4\0\3\10\1\0\4\10" +
                  "\1\115\17\10\13\0\1\10\4\0\3\10\1\0\2\10" +
                  "\1\116\21\10\13\0\1\10\4\0\3\10\1\0\17\10" +
                  "\1\117\4\10\13\0\1\10\4\0\3\10\1\0\3\10" +
                  "\1\120\20\10\13\0\1\10\4\0\3\10\1\0\15\10" +
                  "\1\121\6\10\13\0\1\10\4\0\3\10\1\0\4\10" +
                  "\1\122\17\10\13\0\1\10\4\0\3\10\1\0\3\10" +
                  "\1\123\20\10\13\0\1\10\4\0\3\10\1\0\21\10" +
                  "\1\124\2\10\13\0\1\10\4\0\3\10\1\0\22\10" +
                  "\1\125\1\10\13\0\1\10";

  private static int [] zzUnpackTrans() {
    int[] result = new int[2240];
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
          "\1\0\3\1\1\11\13\1\2\11\1\1\1\11\6\1" +
                  "\1\11\2\1\1\11\2\1\1\11\4\1\1\11\17\1" +
                  "\3\11\10\1\1\11\24\1";

  private static int [] zzUnpackAttribute() {
    int[] result = new int[85];
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
          case 35:
            break;
          case 2: 
            { return com.intellij.psi.TokenType.BAD_CHARACTER;
            }
          case 36:
            break;
          case 3: 
            { return TokenType.WHITE_SPACE;
            }
          case 37:
            break;
          case 4: 
            { return CAP_VAR;
            }
          case 38:
            break;
          case 5: 
            { return LOW_VAR;
            }
          case 39:
            break;
          case 6: 
            { return SYM_OP;
            }
          case 40:
            break;
          case 7: 
            { return LPAREN;
            }
          case 41:
            break;
          case 8: 
            { return RPAREN;
            }
          case 42:
            break;
          case 9: 
            { return LBRACKET;
            }
          case 43:
            break;
          case 10: 
            { return RBRACKET;
            }
          case 44:
            break;
          case 11: 
            { return COMMA;
            }
          case 45:
            break;
          case 12: 
            { return DOT;
            }
          case 46:
            break;
          case 13: 
            { return EQUALS;
            }
          case 47:
            break;
          case 14: {
            return PIPE;
            }
          case 48:
            break;
          case 15: 
            { return COLON;
            }
          case 49:
            break;
          case 16: {
            return RUNE_OF_AUTOCOMPLETION;
            }
          case 50:
            break;
          case 17: {
            yypopstate();
            return TokenType.WHITE_SPACE;
            }
          case 51:
            break;
          case 18: {
            return AS;
            }
          case 52:
            break;
          case 19: {
            yypushstate(INCOMMENT);
            return BEGIN_COMMENT;
            }
          case 53:
            break;
          case 20: {
            return COMMA_OP;
          }
          case 54:
            break;
          case 21: 
            { return OPEN_LISTING;
            }
          case 55:
            break;
          case 22: {
            yypushstate(INLINECOMMENT);
            return LINE_COMMENT;
            }
          case 56: break;
          case 23: {
            return ARROW;
            }
          case 57: break;
          case 24: {
            yypopstate();
            return END_COMMENT;
            }
          case 58: break;
          case 25: {
            yypopstate();
            return END_DOC_COMMENT;
            }
          case 59: break;
          case 26: {
            yypushstate(DOCCOMMENT);
            return BEGIN_DOC_COMMENT;
            }
          case 60: break;
          case 27: {
            return PORT;
            }
          case 61: break;
          case 28: {
            return TYPE;
            }
          case 62: break;
          case 29: {
            return WHERE;
            }
          case 63: break;
          case 30: {
            return ALIAS;
            }
          case 64:
            break;
          case 31: {
            return EFFECT;
            }
          case 65:
            break;
          case 32: {
            return MODULE;
          }
          case 66:
            break;
          case 33: {
            return IMPORT;
          }
          case 67:
            break;
          case 34: 
            { return EXPOSING;
            }
          case 68:
            break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
