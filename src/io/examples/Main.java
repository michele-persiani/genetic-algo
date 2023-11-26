package io.examples;

import io.geneticalgo.GeneticAlgorithm;
import io.geneticalgo.chromosome.BitChromosome;
import io.geneticalgo.chromosome.IChromosome;

import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main
{
    static Logger logger = Logger.getLogger("io.examples.Main");


    public static void main(String[] args)
    {
        BitChromosome prototype = new BitChromosome(100);

        GeneticAlgorithm<Boolean> algo = new GeneticAlgorithm.Builder<Boolean>()
                .populationSize(400)
                .maxEpochs(100)
                .mutationProbability(0.01)
                .keepBestPercentage(0.05)
                .prototype(prototype)
                .fitnessFunction(ch -> (double ) IntStream.range(0, ch.size())
                        .mapToDouble(idx -> ch.getGene(idx)? Math.pow(idx, 2) : 0)
                        .sum())
                .logger(msg -> logger.info(msg))
                .build();

        IChromosome<Boolean> best = algo
                .stream()
                .reduce((first, second) -> second)
                .get()
                .bestChromosome(algo.fitnessFunction);

        logger.info("Best chromosome=%s".formatted(best));

    }
}