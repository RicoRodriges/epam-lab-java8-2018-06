package spliterators.example6;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ParallelBigIntegerFibonacciSpliterator implements Spliterator<BigInteger> {

    private static final int SEQUENTIAL_THRESHOLD = 100;
    private static final BigInteger[] STARTING_MATRIX = {
            BigInteger.ONE, BigInteger.ONE,
            BigInteger.ONE, BigInteger.ZERO};

    private BigInteger[] state;
    private final int endExcluded;
    private int current;

    public ParallelBigIntegerFibonacciSpliterator(int max) {
        this(0, max);
    }

    private ParallelBigIntegerFibonacciSpliterator(int startInclusive, int endExclusive) {
        this.current = startInclusive;
        this.endExcluded = endExclusive;
    }

    private static BigInteger[] multiply(BigInteger[] left, BigInteger[] right) {
        return new BigInteger[]{
                left[0].multiply(right[0]).add(left[1].multiply(right[2])),
                left[0].multiply(right[1]).add(left[1].multiply(right[3])),
                left[2].multiply(right[0]).add(left[3].multiply(right[2])),
                left[2].multiply(right[1]).add(left[3].multiply(right[3]))};
    }

    private static BigInteger[] power(BigInteger[] matrix, int power) {
        if (power == 1) {
            return matrix;
        }
        if (power % 2 == 0) {
            BigInteger[] root = power(matrix, power / 2);
            return multiply(root, root);
        } else {
            return multiply(power(matrix, power - 1), matrix);
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super BigInteger> action) {
        if (current == endExcluded)
            return false;
        if (state == null) {
            if (current == 0) {
                state = new BigInteger[]{BigInteger.ZERO, BigInteger.ONE};
            } else {
                BigInteger[] res = power(STARTING_MATRIX, current);
                state = new BigInteger[]{res[1], res[0]};
            }
        }
        action.accept(state[1]);
        if (++current < endExcluded) {
            BigInteger next = state[0].add(state[1]);
            state[0] = state[1];
            state[1] = next;
        }
        return true;
    }

    @Override
    public Spliterator<BigInteger> trySplit() {
        if (endExcluded - current < 2) {
            return null;
        }
        int mid = (endExcluded + current) >>> 1;
        if (mid - current < SEQUENTIAL_THRESHOLD) {
            BigInteger[] array = new BigInteger[mid - current];
            for (int i = 0; i < array.length; i++) {
                tryAdvance(f -> {});
                array[i] = state[0];
            }
            return Spliterators.spliterator(array, ORDERED | NONNULL | SORTED);
        }
        return new ParallelBigIntegerFibonacciSpliterator(current, current = mid);
    }

    @Override
    public long estimateSize() {
        return endExcluded - current;
    }

    @Override
    public int characteristics() {
        return ORDERED | IMMUTABLE | SIZED | SUBSIZED | NONNULL | SORTED;
    }

    @Override
    public Comparator<? super BigInteger> getComparator() {
        return null;
    }
}
