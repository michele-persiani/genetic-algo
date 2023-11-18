package io.geneticalgo;


import io.geneticalgo.chromosome.IChromosome;

import java.util.function.Function;

/**
 * Fitness function to evaluate chromosomes
 * @param <T>
 */
public interface IFitnessFunction<T> extends Function<IChromosome<T>, Double>
{
}
