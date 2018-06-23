package streams.part2.exercise;

import org.junit.Test;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

public class Exercise3 {

    @Test
    public void createLimitedStringWithOddNumbersSeparatedBySpaces() {
        int countNumbers = 10;

        String result = Stream.iterate(1L, i -> i + 2)
                .limit(countNumbers)
                .map(String::valueOf)
                .collect(joining(" "));

        assertEquals("1 3 5 7 9 11 13 15 17 19", result);
    }

    @Test
    public void extractEvenNumberedCharactersToNewString() {
        String source = "abcdefghijklm";

        String result = Stream.iterate(0, i -> i + 2)
                .limit((source.length() + 1) / 2)
                .map(source::charAt)
                .map(String::valueOf)
                .collect(joining());

        assertEquals("acegikm", result);
    }
}
