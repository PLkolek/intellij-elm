package mkolaczek.util;

import java.util.Optional;
import java.util.stream.Stream;

public class Streams {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Stream<T> stream(Optional<T> opt) {
        return opt.isPresent() ? Stream.of(opt.get()) : Stream.empty();
    }

    public static <T> Stream<T> stream(T nullable) {
        return nullable != null ? Stream.of(nullable) : Stream.empty();
    }
}
