package edus2;

import edus2.domain.Scan;

import java.util.concurrent.ThreadLocalRandom;

public class TestUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_ALPHABET = LOWERCASE_ALPHABET.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUMERIC_CHARACTERS = LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS;

    static String randomAlphanumericString() {
        int stringLength = RANDOM.nextInt(1, 101);
        StringBuilder result = new StringBuilder();
        for(int i = 1; i <= stringLength; i++) {
            int characterIndex = RANDOM.nextInt(0, ALPHANUMERIC_CHARACTERS.length());
            result.append(ALPHANUMERIC_CHARACTERS.charAt(characterIndex));
        }

        return result.toString();
    }

    static Scan randomScan() {
        return new Scan(randomAlphanumericString(), randomAlphanumericString());
    }
}
