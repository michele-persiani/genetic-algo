# Lightweight genetic algorithm 
## See master branch

Lightweight Genetic Algorithm implementation wirtten in Java. Supports custon types of chromosomes, crossovers and selection criteria


Example utilization evolving bit genes into ones containing most ones:


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

