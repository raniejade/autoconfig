package io.polymorphicpanda.autoconfig;

/**
 * Loads the actual implementation of the config interface.
 *
 * @author Ranie Jade Ramiso
 */
public final class AutoConfigLoader {
    public static final String GENERATED_CLASS_SUFFIX = "$$AutoConfig";

    /**
     * Load the actual implementation of <code>cls</code>.
     *
     * @param cls config interface.
     * @param classLoader {@link java.lang.ClassLoader} used to load the class.
     * @return Instance of <code>cls</code>.
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> cls, ClassLoader classLoader) {
        final String generatedClassName = cls.getName() + GENERATED_CLASS_SUFFIX;

        try {
            return (T) classLoader.loadClass(generatedClassName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the actual implementation of <code>cls</code>.
     *
     * <p>Equivalent to: <code>AutoConfigLoader.load(cls, Thread.currentThread().getContextClassLoader())</code></p>
     *
     * @param cls config interface.
     * @return Instance of <code>cls</code>.
     *
     * @see {@link #load(Class, ClassLoader)}
     */
    public static <T> T load(Class<T> cls) {
        return load(cls, Thread.currentThread().getContextClassLoader());
    }

    private AutoConfigLoader() {}
}
