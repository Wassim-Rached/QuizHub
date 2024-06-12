package org.wa55death405.quizhub.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithms {

    public static int countStringOccurrences(String text, String searchString) {
        if (text == null || text.isEmpty() || searchString == null || searchString.isEmpty()) {
            throw new IllegalArgumentException("Text and search string can't be null or empty");
        }

        int count = 0;
        int fromIndex = 0;

        while ((fromIndex = text.indexOf(searchString, fromIndex)) != -1 ) {
            count++;
            fromIndex += searchString.length();
        }

        return count;
    }

    public static int countWordsSeparatedByDelimiter(String input, String delimiter) {
        if (input == null || input.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("Input and delimiter can't be null or empty");
        }

        // Split the input string by the delimiter
        String[] parts = input.split("\\Q" + delimiter + "\\E");

        System.out.println(Arrays.toString(parts));
        // Count the non-empty parts
        int count = 0;
        for (String part : parts) {
            if (!part.isEmpty()) {
                count++;
            }
        }

        return count;
    }

    public static String[] extractWordsSeparatedByDelimiter(String input, String delimiter) {
        if (input == null || input.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("Input and delimiter can't be null or empty");
        }

        // Split the input string by the delimiter
        String[] parts = input.split("\\Q" + delimiter + "\\E");

        // Filter out empty strings
        List<String> nonEmptyParts = new ArrayList<>();
        for (String part : parts) {
            if (!part.isEmpty()) {
                nonEmptyParts.add(part);
            }
        }

        return nonEmptyParts.toArray(new String[0]);
    }
}
