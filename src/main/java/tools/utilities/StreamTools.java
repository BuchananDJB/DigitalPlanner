package tools.utilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class StreamTools {

    public static <T> Stream<T> stream(Collection<T> collection) {
        return collection != null ? collection.stream() : Stream.empty();
    }

    public static Stream<?> stream(Object[] array) {
        return array != null ? Arrays.stream(array) : Stream.empty();
    }

}
