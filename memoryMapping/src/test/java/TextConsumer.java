@FunctionalInterface
public interface TextConsumer<T> {


    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    void consume(T value);
}

