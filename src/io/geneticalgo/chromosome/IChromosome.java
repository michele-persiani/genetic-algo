package io.geneticalgo.chromosome;

import java.util.Collection;


/**
 * Cromosome as a collection of genes.
 * @param <T>
 */
public interface IChromosome<T>
{
    /**
     * Get gene's value
     * @param position gene position
     */
    T getGene(int position);

    /**
     * Set gene's value
     * @param position gene position
     * @param value new gene value. This value should be deep copied
     */
     void setGene(int position, T value);

    /**
     *
     * @return Number of genes in the chromosomes
     */
    int size();

    /**
     * Randomly mutate a gene
     * @param position gene's position
     */
    void mutate(int position);

    /**
     *
     * @return a deep copy of the chromosome
     */
    IChromosome<T> clone();

    /**
     * Fully randomize the chromosome
     */
    void randomize();

}
