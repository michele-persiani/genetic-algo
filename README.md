# Lightweight genetic algorithm 
## See master branch

Lightweight Genetic Algorithm implementation written in Java. Supports custon types of chromosomes, crossovers and selection criteria.


Example utilization evolving bit genes into populations containing most ones:

```

// Algorithm is set up through its builder. Boolean indicates the type of gene inside the chromosomes. Can be any custom class

        BitChromosome prototype = new BitChromosome(100);

        GeneticAlgorithm<Boolean> algo = new GeneticAlgorithm.Builder<Boolean>()
                .populationSize(400)
                .maxEpochs(100)
                .mutationProbability(0.01)
                .keepBestPercentage(0.05)
                .prototype(prototype)
                .selectionStrategy(new RouletteSelection<>())
                .crossoverFunction(CrossoverFactory.pairwiseCrossoverPoint())
                .fitnessFunction(ch -> IntStream.range(0, ch.size())
                        .mapToDouble(idx -> ch.getGene(idx)? Math.pow(idx, 2) : 0)
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
