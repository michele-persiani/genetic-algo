package io.geneticalgo.fitness;

import io.geneticalgo.chromosome.IChromosome;

import java.util.HashMap;
import java.util.Map;

public class MultiObjectiveFitnessFunction<T> implements IFitnessFunction<T>
{
    private final Map<IFitnessFunction<T>, Double> objectives = new HashMap<>();


    public void addObjective(double weight, IFitnessFunction<T> objective)
    {
        objectives.put(objective, weight);
    }


    @Override
    public Double apply(IChromosome<T> tiChromosome)
    {
        double sum = objectives.values().stream().reduce(1e-12, Double::sum);
        Map<IFitnessFunction<T>, Double> normalizeWeights = new HashMap<>();
        objectives.keySet().forEach(func -> normalizeWeights.put(func, objectives.get(func) / sum));


        return normalizeWeights.keySet()
                .stream()
                .map(func -> func.apply(tiChromosome) * normalizeWeights.get(func))
                .reduce(0d, Double::sum);
    }
}
