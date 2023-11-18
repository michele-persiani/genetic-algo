package io.util;

import java.util.Comparator;
import java.util.function.Function;


/**
 * Compares elements based on a score function
 * @param <T>
 */
public class ScoreComparator<T> implements Comparator<T>
{
    private final Function<T, Double> scoreFunction;

    public ScoreComparator(Function<T, Double> scoreFunction)
    {
        this.scoreFunction = scoreFunction;
    }

    @Override
    public int compare(T t0, T t1)
    {
        double score0 = scoreFunction.apply(t0);
        double score1 = scoreFunction.apply(t1);
        double diff = score0 - score1;
        return (int) (diff / Math.max(Math.abs(diff), 1e-8));
    }
}
