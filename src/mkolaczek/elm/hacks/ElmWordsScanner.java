/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mkolaczek.elm.hacks;

import com.intellij.lang.cacheBuilder.VersionedWordsScanner;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.Processor;
import mkolaczek.elm.lexer.ElmLexerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Copy paste of DefaultWordsScanner just to allow indexing of symbols
 */

public class ElmWordsScanner extends VersionedWordsScanner {
    private final Lexer myLexer;
    private final TokenSet myIdentifierTokenSet;
    private final TokenSet myCommentTokenSet;
    private final TokenSet myLiteralTokenSet;
    private final TokenSet mySkipCodeContextTokenSet;
    private final TokenSet myProcessAsWordTokenSet;
    private boolean myMayHaveFileRefsInLiterals;

    /**
     * Creates a new instance of the words scanner.
     *
     * @param lexer              the lexer used for breaking the text into tokens.
     * @param identifierTokenSet the set of token types which represent identifiers.
     * @param commentTokenSet    the set of token types which represent comments.
     * @param literalTokenSet    the set of token types which represent literals.
     */
    public ElmWordsScanner(Lexer lexer, TokenSet identifierTokenSet, TokenSet commentTokenSet,
                           TokenSet literalTokenSet) {
        this(lexer, identifierTokenSet, commentTokenSet, literalTokenSet, TokenSet.EMPTY);
    }

    /**
     * Creates a new instance of the words scanner.
     *
     * @param lexer                   the lexer used for breaking the text into tokens.
     * @param identifierTokenSet      the set of token types which represent identifiers.
     * @param commentTokenSet         the set of token types which represent comments.
     * @param literalTokenSet         the set of token types which represent literals.
     * @param skipCodeContextTokenSet the set of token types which should not be considered as code context.
     */
    public ElmWordsScanner(Lexer lexer, TokenSet identifierTokenSet, TokenSet commentTokenSet,
                           TokenSet literalTokenSet, @NotNull TokenSet skipCodeContextTokenSet) {
        this(lexer, identifierTokenSet, commentTokenSet, literalTokenSet, skipCodeContextTokenSet, TokenSet.EMPTY);
    }

    /**
     * Creates a new instance of the words scanner.
     *
     * @param lexer                   the lexer used for breaking the text into tokens.
     * @param identifierTokenSet      the set of token types which represent identifiers.
     * @param commentTokenSet         the set of token types which represent comments.
     * @param literalTokenSet         the set of token types which represent literals.
     * @param skipCodeContextTokenSet the set of token types which should not be considered as code context.
     * @param processAsWordTokenSet   the set of token types which represent overload operators.
     */
    public ElmWordsScanner(Lexer lexer, TokenSet identifierTokenSet, TokenSet commentTokenSet,
                           TokenSet literalTokenSet, @NotNull TokenSet skipCodeContextTokenSet,
                           @NotNull TokenSet processAsWordTokenSet) {
        myLexer = lexer;
        myIdentifierTokenSet = identifierTokenSet;
        myCommentTokenSet = commentTokenSet;
        myLiteralTokenSet = literalTokenSet;
        mySkipCodeContextTokenSet = skipCodeContextTokenSet;
        myProcessAsWordTokenSet = processAsWordTokenSet;
    }

    @Override
    public void processWords(CharSequence fileText, Processor<WordOccurrence> processor) {
        myLexer.start(fileText);
        WordOccurrence occurrence = new WordOccurrence(fileText, 0, 0, null); // shared occurrence

        IElementType type;
        while ((type = myLexer.getTokenType()) != null) {
            if (myProcessAsWordTokenSet.contains(type)) {
                occurrence.init(fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.CODE);
                processor.process(occurrence);
            } else if (myIdentifierTokenSet.contains(type)) {
                //occurrence.init(fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.CODE);
                //if (!processor.process(occurrence)) return;
                if (!stripWords(processor,
                        fileText,
                        myLexer.getTokenStart(),
                        myLexer.getTokenEnd(),
                        WordOccurrence.Kind.CODE,
                        occurrence,
                        false)) {
                    return;
                }
            } else if (myCommentTokenSet.contains(type)) {
                if (!stripWords(processor,
                        fileText,
                        myLexer.getTokenStart(),
                        myLexer.getTokenEnd(),
                        WordOccurrence.Kind.COMMENTS,
                        occurrence,
                        false)) {
                    return;
                }
            } else if (myLiteralTokenSet.contains(type)) {
                if (!stripWords(processor,
                        fileText,
                        myLexer.getTokenStart(),
                        myLexer.getTokenEnd(),
                        WordOccurrence.Kind.LITERALS,
                        occurrence,
                        myMayHaveFileRefsInLiterals)) {
                    return;
                }
            } else if (!mySkipCodeContextTokenSet.contains(type)) {
                if (!stripWords(processor,
                        fileText,
                        myLexer.getTokenStart(),
                        myLexer.getTokenEnd(),
                        WordOccurrence.Kind.CODE,
                        occurrence,
                        false)) {
                    return;
                }
            }
            myLexer.advance();
        }
    }

    protected static boolean stripWords(Processor<WordOccurrence> processor,
                                        CharSequence tokenText,
                                        int from,
                                        int to,
                                        WordOccurrence.Kind kind,
                                        @NotNull WordOccurrence occurrence,
                                        boolean mayHaveFileRefs
    ) {
        // This code seems strange but it is more effective as Character.isJavaIdentifier_xxx_ is quite costly operation due to unicode
        int index = from;

        ScanWordsLoop:
        while (true) {
            while (true) {
                if (index == to) {
                    break ScanWordsLoop;
                }
                char c = tokenText.charAt(index);
                if (isAsciiIdentifierPart(c) || Character.isJavaIdentifierStart(c) || ElmLexerAdapter.isSymbol(c)) {
                    break;
                }
                index++;
            }
            int wordStart = index;
            while (true) {
                index++;
                if (index == to) {
                    break;
                }
                char c = tokenText.charAt(index);
                if (isAsciiIdentifierPart(c)) {
                    continue;
                }
                if (!Character.isJavaIdentifierPart(c) && !ElmLexerAdapter.isSymbol(c)) {
                    break;
                }
            }
            int wordEnd = index;
            occurrence.init(tokenText, wordStart, wordEnd, kind);

            if (!processor.process(occurrence)) {
                return false;
            }

            if (mayHaveFileRefs) {
                occurrence.init(tokenText, wordStart, wordEnd, WordOccurrence.Kind.FOREIGN_LANGUAGE);
                if (!processor.process(occurrence)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isAsciiIdentifierPart(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '$';
    }

    public void setMayHaveFileRefsInLiterals(boolean mayHaveFileRefsInLiterals) {
        myMayHaveFileRefsInLiterals = mayHaveFileRefsInLiterals;
    }
}
