package io.geneticalgo.crossover;

import io.geneticalgo.chromosome.IChromosome;

import java.util.Collection;
import java.util.function.BiFunction;

public interface ICrossoverFunction<T> extends BiFunction<IChromosome<T>, IChromosome<T>, Collection<IChromosome<T>>>
{
}
