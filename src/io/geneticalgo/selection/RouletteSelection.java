package io.geneticalgo.selection;

import io.util.ScoreComparator;
import io.util.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Simple roulette strategy where elements are sampled with probability linear to their score
 * @param <T>
 */
public class RouletteSelection<T> extends BaseSelectionStrategy<T>
{
    private final Random random = new Random();

    @Override
    protected T sampleElement(Map<T, Double> populationScores)
    {
        double scoresSum = populationScores.values().stream().reduce(0d, Double::sum);

        List<Tuple<T, Double>> sortedElements = populationScores
                .keySet()
                .stream()
                .sorted(new ScoreComparator<>(populationScores::get))
                .map(e -> new Tuple<>(e, populationScores.get(e)))
                .toList();


        double v = random.nextDouble() * scoresSum;
        double curr = 0;
        int idx = 0;
        while(curr < v && idx < sortedElements.size())
        {
            curr += sortedElements.get(idx).second;
            idx ++;
        }

        return  sortedElements.get(idx - 1).first;
    }
}
