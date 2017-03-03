package mkolaczek.util;

import java.util.Optional;
import java.util.stream.Stream;

public class Optionals {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Stream<T> stream(Optional<T> opt) {
        return opt.isPresent() ? Stream.of(opt.get()) : Stream.empty();
    }
}
