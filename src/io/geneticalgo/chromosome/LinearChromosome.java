package io.geneticalgo.chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Chromosome holding genes as a linear structure such as a list
 * @param <T>
 */
public abstract class LinearChromosome<T> implements IChromosome<T>
{
    protected final ArrayList<T> genes = new ArrayList<>();

    public LinearChromosome(int size)
    {
        for(int i = 0; i < size; i ++)
            genes.add(createDefaultGene());
    }

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

    public String toString()
    {
        return "[%s]".formatted(String.join(" ", genes().stream().map(Object::toString).collect(Collectors.toList())));
    }

    protected abstract T createDefaultGene();

    public abstract IChromosome<T> clone();
}
