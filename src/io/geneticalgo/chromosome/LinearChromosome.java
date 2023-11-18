package io.geneticalgo.chromosome;

import java.util.ArrayList;
import java.util.List;


/**
 * Chromosome holding genes as a linear structure such as a list
 * @param <T>
 */
public abstract class LinearChromosome<T> implements IChromosome<T>
{
    protected ArrayList<T> genes = new ArrayList<>();

    public List<T> genes()
    {
        return new ArrayList<>(genes);
    }

    @Override
    public T getGene(int position)
    {
        return genes.get(position);
    }

    @Override
    public void setGene(int position, T value)
    {
        genes.set(position, value);
    }

    @Override
    public int size()
    {
        return genes.size();
    }

    public abstract IChromosome<T> clone();

}
