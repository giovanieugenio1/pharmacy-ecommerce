package com.pharmacy.commerce.shared.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtils {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");

    private SlugUtils() {}

    public static String toSlug(String input) {
        String normalized = Normalizer.normalize(input.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String hyphenated = WHITESPACE.matcher(noAccents).replaceAll("-");
        String cleaned = NON_ALPHANUMERIC.matcher(hyphenated).replaceAll("");
        return MULTIPLE_DASHES.matcher(cleaned).replaceAll("-");
    }
}
