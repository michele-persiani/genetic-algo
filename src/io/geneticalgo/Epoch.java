package io.geneticalgo;

import io.geneticalgo.chromosome.IChromosome;
import io.geneticalgo.fitness.IFitnessFunction;
import io.util.ScoreComparator;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Epoch class containing the epoch's population
 * @param <T>
 */
public class Epoch<T>
{
    private final Collection<IChromosome<T>> population;

    public Epoch(Collection<IChromosome<T>> population)
    {
        this.population = new ArrayList<>(population);
    }

    public IChromosome<T> bestChromosome(IFitnessFunction<T> fitnessFunction)
    {
        return population.isEmpty()? null : population.stream().max(new ScoreComparator<>(fitnessFunction)).get();
    }

    public double bestFitness(IFitnessFunction<T> fitnessFunction)
    {
        return fitnessFunction.apply(bestChromosome(fitnessFunction));
    }

    public double averageFitness(IFitnessFunction<T> fitnessFunction)
    {
        return population.stream().mapToDouble(fitnessFunction::apply).average().getAsDouble();
    }
}
