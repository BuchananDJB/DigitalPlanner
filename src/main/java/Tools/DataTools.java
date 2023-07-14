package Tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class DataTools {
    public static Stream<?> stream(Collection<?> collection) {
        return collection != null ? collection.stream() : Stream.empty();
    }

    public static Stream<?> stream(Object[] array) {
        return array != null ? Arrays.stream(array) : Stream.empty();
    }

    public static boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }
}
