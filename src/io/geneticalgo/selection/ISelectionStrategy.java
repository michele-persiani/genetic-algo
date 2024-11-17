package io.geneticalgo.selection;

import java.util.Enumeration;
import java.util.Map;

/**
 * Sample strategy to select which chromosome will be crossovered. It is an enumeration where each returned element
 * comes from sampling the population
 * @param <T> population type
 */
public interface ISelectionStrategy<T> extends Enumeration<T>
{
    /**
     * Sets the population of this selection strategy, to sample using the Enumeration interface
     * @param population population and associated scores to sample from
     */
    void setPopulation(Map<T, Double> population);
}
