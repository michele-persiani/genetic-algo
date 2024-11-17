package io.geneticalgo.selection;

public class SelectionStrategyFactory
{
    private SelectionStrategyFactory() {}

    public static <T> ISelectionStrategy<T> createRouletteSelection()
    {
        return new RouletteSelection<>();
    }
}
