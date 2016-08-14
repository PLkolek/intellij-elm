package mkolaczek.elm.autocompletion;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Names {
    @NotNull
    public static String concatLast(@NotNull String[] words,
                                    int wordCount) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(words[words.length - wordCount]);

        for (int i = words.length - wordCount + 1; i < words.length; i++) {
            buffer.append(words[i]);
        }

        return buffer.toString();
    }

    @NotNull
    static List<LookupElementBuilder> suggest(String[] words) {
        List<LookupElementBuilder> res = Lists.newArrayList();
        if (words.length > 1) {
            for (int numWords = 1; numWords < words.length; numWords++) {
                String suggestedName = concatLast(words, numWords);
                res.add(LookupElementBuilder.create(suggestedName));
            }
        }

        List<String> previousWords = Arrays.asList(words).subList(0, words.length - 1);
        Stream<LookupElementBuilder> suggestions = previousWords.stream().map(LookupElementBuilder::create);
        res.addAll(suggestions.collect(Collectors.toList()));
        return res;
    }
}
