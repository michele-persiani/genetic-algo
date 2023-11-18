import io.geneticalgo.GeneticAlgorithm;
import io.geneticalgo.chromosome.BitChromosome;
import io.geneticalgo.chromosome.IChromosome;

import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main
{
    static Logger logger = Logger.getLogger("Main");


    public static void main(String[] args)
    {
        BitChromosome prototype = new BitChromosome(100);

        GeneticAlgorithm<Boolean> algo = new GeneticAlgorithm.Builder<Boolean>()
                .prototype(prototype)
                .populationSize(800)
                .mutationProbability(0)
                .maxEpochs(1000)
                .fitnessFunction(ch -> (double ) IntStream.range(0, ch.size())
                        .map(idx -> (int)(Math.pow(idx, 2) * (ch.getGene(idx)? 1 : 0)))
                        .sum())
                .logger(msg -> logger.info(msg))
                .build();

        IChromosome<Boolean> best = algo
                .stream()
                .reduce((first, second) -> second)
                .get()
                .bestChromosome(algo.fitnessFunction);
    }
}