package io.geneticalgo.chromosome;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


/**
 * Bit chromosome
 */
public class BitChromosome extends LinearChromosome<Boolean>
{
    private final Random random = new Random();

    public BitChromosome(int size)
    {
        for(int i = 0; i < size; i ++)
            genes.add(random.nextBoolean());
    }


    @Override
    public void mutate(int position)
    {
        genes.set(position, !genes.get(position));
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

    @Override
    public Collection<IChromosome<Boolean>> crossover(IChromosome<Boolean> other)
    {
        int crossoverPoint = random.nextInt(size());

        IChromosome<Boolean> c0 = ((IChromosome<Boolean>) this).clone();
        IChromosome<Boolean> c1 = other.clone();

        for(int i = 0; i < crossoverPoint; i++)
        {
            Boolean tmp = c0.getGene(i);
            c0.setGene(i, c1.getGene(i));
            c1.setGene(crossoverPoint, tmp);
        }
        ArrayList<IChromosome<Boolean>> result = new ArrayList<>();
        result.add(c0);
        result.add(c1);
        return result;
    }
}
