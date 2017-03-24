package mkolaczek.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Collectors {

    public static <E, K, V> Collector<E, Multimap<K, V>, Multimap<K, V>> toMultimap(Function<E, K> keyGetter,
                                                                                    Function<E, V> valueGetter) {
        return new Collector<E, Multimap<K, V>, Multimap<K, V>>() {
            @Override
            public Supplier<Multimap<K, V>> supplier() {
                return HashMultimap::create;
            }

            @Override
            public BiConsumer<Multimap<K, V>, E> accumulator() {
                return (m, e) -> m.put(keyGetter.apply(e), valueGetter.apply(e));
            }

            @Override
            public BinaryOperator<Multimap<K, V>> combiner() {
                return (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                };
            }

            @Override
            public Function<Multimap<K, V>, Multimap<K, V>> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Sets.newHashSet(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
            }
        };
    }
}
