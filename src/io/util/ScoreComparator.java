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
    private final int inverted;

    public ScoreComparator(Function<T, Double> scoreFunction)
    {
        this(scoreFunction, false);
    }

    public ScoreComparator(Function<T, Double> scoreFunction, boolean inverted)
    {
        this.scoreFunction = scoreFunction;
        this.inverted = inverted ? -1 : 1;
    }

    @Override
    public int compare(T t0, T t1)
    {
        double score0 = scoreFunction.apply(t0);
        double score1 = scoreFunction.apply(t1);
        return  inverted * (int) (score0 - score1);
    }
}
