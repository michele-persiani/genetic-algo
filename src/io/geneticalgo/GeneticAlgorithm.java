package io.geneticalgo;

import io.geneticalgo.chromosome.IChromosome;
import io.geneticalgo.crossover.CrossoverFactory;
import io.geneticalgo.crossover.ICrossoverFunction;
import io.geneticalgo.fitness.IFitnessFunction;
import io.geneticalgo.selection.ISelectionStrategy;
import io.geneticalgo.selection.RouletteSelection;
import io.util.ScoreComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Genetic Algorithm class. Use GeneticAlgorithm.Builder to setup the instance initial parameters such as the chromosome
 * prototype, num of epochs, fitness function, etc.
 * After creation the algorithm in run as an enumerator. Each element of the enumeration will reflect a computation
 * of an epoch. There is a convenience method stream() for streaming the epochs.
 * @param <T> gene type
 */
public class GeneticAlgorithm<T> implements Enumeration<Epoch<T>>
{

    public static class Builder<T> extends io.util.Builder<GeneticAlgorithm<T>>
    {
        public Builder<T> populationSize(int popSize)
        {
            return this.setValue(this, a -> a.populationSize = Math.max(1, popSize));
        }

        public Builder<T> maxEpochs(int maxEpochs)
        {
            return this.setValue(this, a -> a.maxEpochs = Math.max(1, maxEpochs));
        }

        public Builder<T> mutationProbability(double probability)
        {
            return this.setValue(this, a -> a.mutationProbability = clamp(probability, 0, 1));
        }

        public Builder<T> keepBestPercentage(double percentage)
        {
            return this.setValue(this, a -> a.keepBestPercentage = clamp(percentage, 0, 1));
        }

        public Builder<T> prototype(IChromosome<T> prototype)
        {
            return this.setValue(this, a -> a.prototype = prototype);
        }

        public Builder<T> selectionStrategy(ISelectionStrategy<IChromosome<T>> selectionStrategy)
        {
            return this.setValue(this, a -> a.selectionStrategy = selectionStrategy);
        }

        public Builder<T> fitnessFunction(IFitnessFunction<T> fitnessFunction)
        {
            return this.setValue(this, a -> a.fitnessFunction = fitnessFunction);
        }

        public Builder<T> crossoverFunction(ICrossoverFunction<T> crossoverFunction)
        {
            return this.setValue(this, a -> a.crossoverFunction = crossoverFunction);
        }

        public Builder<T> logger(Consumer<String> logger)
        {
            return this.setValue(this, a -> a.logger = logger);
        }

        @Override
        protected GeneticAlgorithm<T> newInstance()
        {
            return new GeneticAlgorithm<>();
        }
    }

    /**
     * Population size
     */
    public int populationSize = 200;

    /**
     * Max number of epochs
     */
    public int maxEpochs = 100;

    /**
     * Probability that single genes will mutate after crossover
     */
    public double mutationProbability = 0.02d;

    /**
     * Percentage of best chromosomes that are kept between an epoch and the other
     */
    public double keepBestPercentage = 0.05d;

    /**
     * Selection strategy to sample crossovered chromosomes
     */
    public ISelectionStrategy<IChromosome<T>> selectionStrategy = new RouletteSelection<>();

    /**
     * Prototype to initialize the population
     */
    public IChromosome<T> prototype = null;

    /**
     * Crossover function
     */
    private ICrossoverFunction<T> crossoverFunction = CrossoverFactory.createPairwiseCrossoverPoint();

    /**
     * Chromosome fitness function
     */
    public IFitnessFunction<T> fitnessFunction = ch -> 0d;

    /**
     * Logger function
     */
    private Consumer<String> logger = s -> {};

    /**
     * Holds the current epoch's population. This gets set at every epoch
     */
    private Collection<IChromosome<T>> currentPopulation = new HashSet<>();

    /**
     * Holds each computed epoch
     */
    private final List<Epoch<T>> epochs = new ArrayList<>();

    private final Random random = new Random();

    /**
     * Algorithm's start time. Set at the beginning of computations
     */
    private long trainingStartTime = 0L;


    /**
     * Reset the algorithm for a new computations
     */
    public void reset()
    {
        epochs.clear();
        trainingStartTime = 0L;
    }

    /**
     *
     * @return current epoch
     */
    public int currentEpoch()
    {
        return epochs.size();
    }

    /**
     *
     * @return whether there are more epochs to compute, depending on current epoch and max epoch
     */
    @Override
    public boolean hasMoreElements()
    {
        return currentEpoch() < maxEpochs;
    }



    @Override
    public Epoch<T> nextElement()
    {
        if(currentEpoch() == 0)
        {
            initializePopulation();
            trainingStartTime = System.currentTimeMillis();
        }
        long epochStartTime = System.currentTimeMillis();

        if(!hasMoreElements())
            throw new NoSuchElementException("Max epoch reached");

        List<IChromosome<T>> nextPopulation = new ArrayList<>();


        Map<IChromosome<T>, Double> fitnesses = new ConcurrentHashMap<>();

        currentPopulation.forEach(ch -> {
                fitnesses.put(ch, fitnessFunction.apply(ch));
        });


        // Keep best individuals
        fitnesses.keySet()
                .stream()
                 .sorted(new ScoreComparator<>(fitnesses::get, true))
                .limit((long)clamp(Math.ceil(keepBestPercentage * populationSize), 0, populationSize))
                .forEach(nextPopulation::add);

        // Crossover
        selectionStrategy.setPopulation(fitnesses);

        while(nextPopulation.size() < populationSize && selectionStrategy.hasMoreElements())
        {
            IChromosome<T> sel0 = selectionStrategy.nextElement();
            IChromosome<T> sel1 = selectionStrategy.nextElement();

            crossoverFunction
                    .apply(sel0, sel1)
                    .stream()
                    .peek(ch -> {
                        // Mutation
                        IntStream.range(0, ch.size())
                                .filter(idx -> random.nextDouble() < mutationProbability)
                                .forEach(ch::mutate);
                    }).forEach(nextPopulation::add);
        }

        currentPopulation = nextPopulation.stream().limit(populationSize).collect(Collectors.toList());

        double currentTimeMillis   = System.currentTimeMillis();
        double trainingElapsedTime = (currentTimeMillis - trainingStartTime) / 1000d;
        double epochElapsedTime    = (currentTimeMillis - epochStartTime) / 1000d;

        Epoch<T> epoch = new Epoch<>(currentPopulation);

        double bestFitness = epoch.bestFitness(fitnessFunction);
        double avgFitness = epoch.averageFitness(fitnessFunction);
        logMessage(
                "EPOCH=%-10s AVG FITNESS=%-15s BEST FITNESS=%-15s ELAPSED TIME=%-10s  (EPOCH=%ss)",
                epochs.size(),
                avgFitness,
                bestFitness,
                String.format("%ss", trainingElapsedTime),
                epochElapsedTime
                );
        epochs.add(epoch);
        return epoch;
    }

    /**
     *
     * @return a stream for the computed epochs
     */
    public Stream<Epoch<T>> stream()
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<>()
        {
            @Override
            public Epoch<T> next()
            {
                return nextElement();
            }

            @Override
            public boolean hasNext()
            {
                return hasMoreElements();
            }
        }, Spliterator.ORDERED), false);
    }

    /**
     * Population random initialization
     */
    private void initializePopulation()
    {
        currentPopulation.clear();
        if (prototype == null)
            throw new IllegalStateException("prototype == null");
        IChromosome<T> curr = prototype.clone();
        for(int i = 0; i < populationSize; i++)
        {
            curr.randomize();
            currentPopulation.add(curr);
            curr = prototype.clone();
        }

    }

    /**
     * Log message
     * @param msg message to log. Can contain '%s' tokens for printing arguments with String.format()
     * @param args arguments to format the string with
     */
    private void logMessage(String msg, Object... args)
    {
        logger.accept(String.format(msg, args));
    }

    private static double clamp(double value, double minValue, double maxValue)
    {
        return Math.max(minValue, Math.min(value, maxValue));
    }
}
