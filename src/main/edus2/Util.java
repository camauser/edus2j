package edus2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    @SafeVarargs
    public static <T> List<T> Lst(T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }
}
