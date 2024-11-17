package io.geneticalgo.chromosome;

import io.geneticalgo.crossover.CrossoverFactory;

import java.util.Collection;
import java.util.Random;

/**
 * Chromosome of doubles
 */

public class DoubleChromosome extends LinearChromosome<Double>
{
    private final double mutationRange;
    private final double initMinValue;
    private final double initMaxValue;
    private static final Random random = new Random();

    public DoubleChromosome(int size, double initMinValue, double initMaxValue, double mutationRange)
    {
        super(size);
        this.initMinValue = initMinValue;
        this.initMaxValue = initMaxValue;
        this.mutationRange = mutationRange;
    }

    @Override
    public void mutate(int position)
    {
        double newValue = getGene(position) + (2 * random.nextDouble() - 1) * mutationRange;
        setGene(position, newValue);
    }

    @Override
    public void randomize()
    {
        for(int i = 0; i <size(); i++)
            setGene(i, initMinValue + random.nextDouble() * (initMaxValue - initMinValue));
    }

    @Override
    protected Double createDefaultGene()
    {
        return 0d;
    }


    @Override
    public IChromosome<Double> clone()
    {
        IChromosome<Double> clone = new DoubleChromosome(size(), initMinValue, initMaxValue, mutationRange);
        for(int i = 0; i <size(); i++)
            clone.setGene(i, getGene(i));
        return clone;
    }
}
