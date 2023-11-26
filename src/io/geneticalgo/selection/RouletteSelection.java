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

    private Map<T, Double> populationScores = new HashMap<>();

    private Random random = new Random();

    @Override
    public void setPopulation(Map<T, Double> population)
    {
        populationScores = new HashMap<>(population);
    }

    @Override
    public boolean hasMoreElements()
    {
        return !populationScores.isEmpty();
    }

    @Override
    public T nextElement()
    {
        double sum = populationScores.values().stream().reduce(0d, Double::sum);

        Map<T, Double> elementsProbability = new HashMap<>();
        populationScores.keySet().forEach(k -> elementsProbability.put(k, populationScores.get(k) / sum));

        List<T> sortedElements = elementsProbability
                .keySet()
                .stream()
                .sorted(new ScoreComparator<>(elementsProbability::get))
                .collect(Collectors.toList());

        List<Double> sortedProbas = sortedElements
                .stream()
                .map(elementsProbability::get)
                .collect(Collectors.toList());

        double v = random.nextDouble();
        double curr = 0;
        int idx = 0;
        while(curr < v && idx < sortedElements.size())
        {
            curr += sortedProbas.get(idx);
            idx ++;
        }

        T sample =  sortedElements.get(idx - 1);
        if(!replacement)
            populationScores.remove(sample);
        return sample;
    }
}
