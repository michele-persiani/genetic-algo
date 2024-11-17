package io.geneticalgo.selection;

import java.util.*;

/**
 * Base class for ISelectionStrategy implementing replacement
 * @param <T>
 */
public abstract class BaseSelectionStrategy<T> implements ISelectionStrategy<T>
{
    public boolean replacement = true;

    private Map<T, Double> populationScores = new HashMap<>();

    public void setPopulation(Map<T, Double> population)
    {
        populationScores = new HashMap<>(population);
    }

    public boolean isWithReplacement()
    {
        return replacement;
    }

    public void setWithReplacement(boolean withReplacement)
    {
        replacement = withReplacement;
    }

    @Override
    public boolean hasMoreElements()
    {
        return !populationScores.isEmpty();
    }

    @Override
    public T nextElement()
    {
        T sample =  sampleElement(new HashMap<>(populationScores));
        if(!replacement)
            populationScores.remove(sample);
        return sample;
    }

    /**
     * Sample one element from the given population. Usually, elements likelihood of being sampled is proportianal to
     * their score
     * @param population population with scores to sample from
     * @return
     */
    protected abstract T sampleElement(Map<T, Double> population);
}
