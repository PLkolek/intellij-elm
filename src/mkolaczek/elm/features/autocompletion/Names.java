package mkolaczek.elm.features.autocompletion;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Names {
    @NotNull
    private static String concatLast(@NotNull String[] words,
                                     int wordCount) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(words[words.length - wordCount]);

        for (int i = words.length - wordCount + 1; i < words.length; i++) {
            buffer.append(words[i]);
        }

        return buffer.toString();
    }

    @NotNull
    public static Stream<String> suggest(String[] words) {
        List<String> res = Lists.newArrayList();
        if (words.length > 1) {
            for (int numWords = 1; numWords < words.length; numWords++) {
                res.add(concatLast(words, numWords));
            }
        }

        List<String> previousWords = Arrays.asList(words).subList(0, words.length - 1);
        res.addAll(previousWords);
        return res.stream();
    }

    public static String suffix(String name, String prefix) {
        return name.substring(prefix.length()).replaceAll("^\\.+", "");
    }
}
