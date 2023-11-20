# Lightweight genetic algorithm 
## See master branch

Lightweight Genetic Algorithm implementation written in Java. Supports custon types of chromosomes, crossovers and selection criteria.


Example utilization evolving bit genes into populations containing most ones:

```
// Algorithm is set up through its builder. Boolean indicates the type of gene inside the chromosomes. Can be any custom class
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
        // Algorithm is run through the stream() api. Each element of the stream is a Epoch<Boolean> containing the population at that epoch
        IChromosome<Boolean> best = algo
                .stream()
                .reduce((first, second) -> second)
                .get()
                .bestChromosome(algo.fitnessFunction);
```
