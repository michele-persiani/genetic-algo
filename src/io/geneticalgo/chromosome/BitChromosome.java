package io.geneticalgo.chromosome;


import java.util.Random;


/**
 * Chromosome of booleans
 */
public class BitChromosome extends LinearChromosome<Boolean>
{
    private static final Random random = new Random();

    public BitChromosome(int size)
    {
        super(size);
    }


    @Override
    public void mutate(int position)
    {
        genes.set(position, !genes.get(position));
    }

    @Override
    protected Boolean createDefaultGene()
    {
        return false;
    }

    @Override
    public IChromosome<Boolean> clone()
    {
        BitChromosome ch = new BitChromosome(0);
        ch.genes.addAll(genes);
        return ch;
    }

    @Override
    public void randomize()
    {
        int size = size();
        genes.clear();
        for(int i = 0; i < size; i ++)
            genes.add(random.nextBoolean());
    }
}
