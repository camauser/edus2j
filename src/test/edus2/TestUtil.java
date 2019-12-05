package edus2;

import edus2.domain.MannequinScanEnum;
import edus2.domain.Scan;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_ALPHABET = LOWERCASE_ALPHABET.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUMERIC_CHARACTERS = LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS;

    @SafeVarargs
    public static <T> List<T> Lst(T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    public static String randomAlphanumericString() {
        int stringLength = randomInt(1, 101);
        StringBuilder result = new StringBuilder();
        for(int i = 1; i <= stringLength; i++) {
            int characterIndex = RANDOM.nextInt(0, ALPHANUMERIC_CHARACTERS.length());
            result.append(ALPHANUMERIC_CHARACTERS.charAt(characterIndex));
        }

        return result.toString();
    }

    public static int randomInt() {
        return randomInt(0, Integer.MAX_VALUE - 1);
    }

    public static int randomInt(int lowerInclusive, int upperInclusive) {
        return RANDOM.nextInt(lowerInclusive, upperInclusive + 1);
    }

    public static Scan randomScan() {
        return new Scan(randomMannequinScanEnum(), randomAlphanumericString());
    }

    public static MannequinScanEnum randomMannequinScanEnum() {
        MannequinScanEnum[] values = MannequinScanEnum.values();
        return values[ThreadLocalRandom.current().nextInt(0, values.length)];
    }

    public static String randomTempFile() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        return fileName;
    }

}
