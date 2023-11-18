package io.geneticalgo.selection;

import io.util.ScoreComparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Simple roulette strategy where elements are sampled with probability linear to their score
 * @param <T>
 */
public class RouletteSelection<T> implements ISelectionStrategy<T>
{
    public boolean replacement = true;

    private Map<T, Double> currentPopulation = new HashMap<>();

    private Random random = new Random();

    @Override
    public void setPopulation(Map<T, Double> population)
    {
        currentPopulation = new HashMap<>(population);
        currentPopulation.keySet().forEach(k -> currentPopulation.put(k, currentPopulation.get(k)));
        double sum = currentPopulation.values().stream().reduce(0d, Double::sum);
        currentPopulation.keySet().forEach(k -> currentPopulation.put(k, currentPopulation.get(k) / sum));
    }

    @Override
    public boolean hasMoreElements()
    {
        return !currentPopulation.isEmpty();
    }

    @Override
    public T nextElement()
    {
        List<T> sortedElements = currentPopulation.keySet()
                                                  .stream()
                                                  .sorted(new ScoreComparator<>(e -> currentPopulation.get(e)))
                                                  .collect(Collectors.toList());

        List<Double> sortedProbas = currentPopulation.values()
                                                  .stream()
                                                  .sorted(new ScoreComparator<>(e -> e))
                                                  .collect(Collectors.toList());

        double v = random.nextDouble();
        double curr = 0;
        int idx;
        for(idx = 0; curr < v; idx ++)
            curr += sortedProbas.get(idx);


        T sample =  sortedElements.get(idx - 1);
        if(!replacement)
            currentPopulation.remove(sample);
        return sample;
    }
}
