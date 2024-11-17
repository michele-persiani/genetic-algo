package io.geneticalgo.fitness;

import io.geneticalgo.chromosome.IChromosome;

import java.util.Arrays;
import java.util.function.Function;

public class FitnessFunctionFactory
{
    private FitnessFunctionFactory() {}



    public <T> IFitnessFunction<T> createFitnessFunction(Function<IChromosome<T>, Double> fun)
    {
        return fun::apply;
    }

    public <T> IFitnessFunction<T> createMultiObjectiveFitnessFunction(Function<IChromosome<T>, Double> ...funs)
    {
        MultiObjectiveFitnessFunction<T> moff = new MultiObjectiveFitnessFunction<>();
        Arrays.stream(funs).forEach(fun -> moff.addObjective(1, fun::apply));
        return moff;
    }
}
