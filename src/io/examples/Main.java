package io.examples;

import io.geneticalgo.GeneticAlgorithm;
import io.geneticalgo.chromosome.BitChromosome;
import io.geneticalgo.chromosome.IChromosome;
import io.geneticalgo.crossover.CrossoverFactory;
import io.geneticalgo.selection.SelectionStrategyFactory;

import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main
{
    public static void main(String[] args)
    {
        Logger logger = Logger.getLogger("Main");

        BitChromosome prototype = new BitChromosome(100);

        GeneticAlgorithm<Boolean> algo = new GeneticAlgorithm.Builder<Boolean>()
                .populationSize(400)
                .maxEpochs(100)
                .mutationProbability(0.01)
                .keepBestPercentage(0.05)
                .prototype(prototype)
                .selectionStrategy(SelectionStrategyFactory.createRouletteSelection())
                .crossoverFunction(CrossoverFactory.createPairwiseCrossoverPoint())
                .fitnessFunction(ch -> IntStream.range(0, ch.size())
                        .mapToDouble(idx -> ch.getGene(idx)? Math.pow(idx, 2) : 0)
                        .sum())
                .logger(logger::info)
                .build();

        IChromosome<Boolean> best = algo
                .stream()
                .reduce((first, second) -> second)
                .get()
                .bestChromosome(algo.fitnessFunction);

        logger.info("Best chromosome=%s".formatted(best));

    }
}