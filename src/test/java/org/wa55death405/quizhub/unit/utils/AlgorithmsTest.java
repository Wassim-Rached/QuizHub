package org.wa55death405.quizhub.unit.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wa55death405.quizhub.utils.Algorithms;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    @Test
    void countStringOccurrences() {
        Assertions.assertEquals(2, Algorithms.countStringOccurrences("hello world hello world","hello"));
        assertEquals(0, Algorithms.countStringOccurrences("heXlo world heXlo world","hello"));
        assertEquals(1, Algorithms.countStringOccurrences("hello world bye world","hello"));
    }

    @Test
    void countWordsSeparatedByDelimiter() {
        assertEquals(4, Algorithms.countWordsSeparatedByDelimiter("hello(|)world(|)hello(|)world","(|)"));
        assertEquals(4, Algorithms.countWordsSeparatedByDelimiter("(|)(|)hello(|)world(|)hello(|)(|)world(|)","(|)"));
        assertEquals(0, Algorithms.countWordsSeparatedByDelimiter("(|)(|)(|)","(|)"));
    }

    @Test
    void extractWordsSeparatedByDelimiter() {
        String[] expected = {"hello","world","hello","world"};
        String[] actual = Algorithms.extractWordsSeparatedByDelimiter("(|)(|)hello(|)(|)(|)world(|)hello(|)(|)world(|)","(|)");
        for (int i = 0 ; i < expected.length ; i++) {
            assertEquals(expected[i],actual[i]);
        }

        assertEquals(0, Algorithms.extractWordsSeparatedByDelimiter("(|)(|)(|)","(|)").length);
    }
}