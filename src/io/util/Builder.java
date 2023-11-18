package io.util;

import java.util.function.Consumer;

/**
 * Base class for builders
 * @param <T> class of built objects
 */
public abstract class Builder<T>
{
    private T object;

    public Builder()
    {
        object = newInstance();
    }


    /**
     * Create new instance of the object to be built;
     * @return
     */
    protected abstract T newInstance();

    /**
     * Sets the object's fields and returns this instance
     * @param builder this instance
     * @param setter setter of the built object fields
     * @return this instance
     * @param <C> class fo the builder's subclass
     */
    protected <C extends Builder<T>> C setValue(C builder, Consumer<T> setter)
    {
        setter.accept(object);
        return builder;
    }

    /**
     * Returns the built object and reset the builder's state
     * @return the built objec
     */
    public T build()
    {
        T result = object;
        object = newInstance();
        return result;
    }

}
