package mkolaczek.util;

import java.util.Optional;
import java.util.stream.Stream;

public class Streams {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Stream<T> stream(Optional<T> opt) {
        return opt.map(Stream::of).orElseGet(Stream::empty);
    }

}
