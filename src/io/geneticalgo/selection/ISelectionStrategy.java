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
     * Sets the population to sample
     * @param population population and associated fitnesses to sample
     */
    void setPopulation(Map<T, Double> population);
}
