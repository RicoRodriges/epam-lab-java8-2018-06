package spliterators.example1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.groupingByConcurrent;

public class Example1 {

    /**
     * [ 0, 1, 2, 3, 4, 5, 6, 7 ]
     * .parallelStream() <- ForkJoinPool.commonPool() <- availableProcessors() - 1
     * .map()
     * .filter()
     * .collect(toSet())
     *
     * parallel reduce
     * [ 0, 1 ] [ 2, 3 ] [ 4, 5 ] [ 6, 7 ] <- chunks
     * {}       {}       {}       {}
     * {0}      {2}      {4}      {6}
     * {0, 1}   {2, 3}   {4, 5}   {6, 7}
     *   {0, 1, 2, 3}      {4, 5, 6, 7}
     *      {0, 1, 2, 3, 4, 5, 6, 7} <- result
     *
     *
     * concurrent reduce
     * [ 0, 1 ] [ 2, 3 ] [ 4, 5 ] [ 6, 7 ] <- chunks
     * {} <- ConcurrentSkipListSet::new
     * {4} <- 2
     * {4, 2}
     * {4, 2, 0}
     * {4, 2, 0, 5}
     * ...
     * {4, 2, 0, 5, 3, 1, 6, 7}
     */


    /**
     * []
     *
     *
     *
     *
     */


    /**
     * Выбирает из текста наиболее часто встречающиеся слова.
     * Подсчет слов выполняется без учета их регистра, т.е. "Привет", "привет", "пРиВеТ" - одно и то же слово.
     * Если некоторые слова имеют одинаковую частоту, то в выходном списке они упорядочиваются в лексикографическом порядке.
     * @param text Исходный текст в котором слова (в смешанном регистре) разделены пробелами.
     * @param numberWords Количество наиболее часто встречающихся слов, которые необходимо отобрать.
     * @return Список отобранных слов (в нижнем регистре).
     */
    private List<String> getFrequentlyOccurringWords(String text, int numberWords) {
        return Pattern.compile("\\s+")
                      .splitAsStream(text)
                      .parallel()
                      .filter(Pattern.compile("\\w+").asPredicate())
                      .map(String::toLowerCase)
                      .collect(groupingByConcurrent(identity(), counting()))
                      .entrySet()
                      .stream()
                      .sorted(Comparator.<Entry<String, Long>>comparingLong(Entry::getValue).reversed().thenComparing(Entry::getKey))
                      .limit(numberWords)
                      .map(Entry::getKey)
                      .collect(Collectors.toList());


    }
}
