package io.geneticalgo.crossover;

import io.geneticalgo.chromosome.IChromosome;

import java.util.ArrayList;
import java.util.Random;

public class CrossoverFactory
{
    /**
     * Gene selector to define multiple crossover behaviours implementing a crossover point strategy
     */
    public interface ICrossoverPointGeneSelector
    {
        /**
         * Selects the gene index to swap
         * @param fromGeneIndexStart indeces inclusive minimum bound
         * @param fromGeneIndexEnd   indeces exclusive maximum bound
         * @param currentGenePosition current gene index
         * @return
         */
        int selectGene(int fromGeneIndexStart, int fromGeneIndexEnd, int currentGenePosition);
    }



    private static final Random random = new Random();

    private CrossoverFactory() {}


    /**
     * Applies crossover between ch0 and ch1 with behaviour dependent on the given geneSelector
     * @param geneSelector
     * @return a collection of chromosomes resulting from the crossover. Most of the times it will be of size 2
     * @param <T>
     */
    public static <T> ICrossoverFunction<T> createCrossoverPoint(ICrossoverPointGeneSelector geneSelector)
    {
        return (ch0, ch1) -> {

            int size = ch0.size();
            int crossoverPoint = random.nextInt(size);

            IChromosome<T> c0 = ch0.clone();
            IChromosome<T> c1 = ch1.clone();

            for(int i = crossoverPoint; i < size; i++)
            {
                int sel0 = geneSelector.selectGene(crossoverPoint, size, i);
                int sel1 = geneSelector.selectGene(crossoverPoint, size, i);
                T tmp = c0.getGene(sel0);
                c0.setGene(i, c1.getGene(sel1));
                c1.setGene(i, tmp);
            }
            ArrayList<IChromosome<T>> result = new ArrayList<>();
            result.add(c0);
            result.add(c1);
            return result;
        };
    }

    /**
     * Sets a random crossover point and starting from it and counting up exchanges genes in a pairwise manner
     * @return a collection of chromosomes resulting from the crossover. Most of the times it will be of size 2
     * @param <T>
     */
    public static <T> ICrossoverFunction<T> createPairwiseCrossoverPoint()
    {
        return createCrossoverPoint((fromGeneIndexStart, fromGeneIndexEnd, toGenePosition) -> toGenePosition);
    }

    /**
     * Sets a random crossover point and starting from it and counting up exchanges genes in a randomized manner
     * between the chromosomes
     * @return a collection of chromosomes resulting from the crossover. Most of the times it will be of size 2
     * @param <T>
     */
    public static <T> ICrossoverFunction<T> createCrossoverPointRandomized()
    {
        return createCrossoverPoint(
                (fromGeneIndexStart, fromGeneIndexEnd, toGenePosition) -> fromGeneIndexStart + random.nextInt(fromGeneIndexEnd - fromGeneIndexStart)
        );
    }
}
