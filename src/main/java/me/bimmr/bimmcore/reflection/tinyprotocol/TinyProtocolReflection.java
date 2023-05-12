package me.bimmr.bimmcore.reflection.tinyprotocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

/**
 * The type Tiny protocol reflection.
 */
public final class TinyProtocolReflection {
    /**
     * The interface Constructor invoker.
     */
    public interface ConstructorInvoker {
        /**
         * Invoke object.
         *
         * @param arguments the arguments
         * @return the object
         */
        public Object invoke(Object... arguments);
    }

    /**
     * The interface Method invoker.
     */
    public interface MethodInvoker {
        /**
         * Invoke object.
         *
         * @param target    the target
         * @param arguments the arguments
         * @return the object
         */
        public Object invoke(Object target, Object... arguments);
    }

    /**
     * The interface Field accessor.
     *
     * @param <T> the type parameter
     */
    public interface FieldAccessor<T> {
        /**
         * Get t.
         *
         * @param target the target
         * @return the t
         */
        public T get(Object target);

        /**
         * Set.
         *
         * @param target the target
         * @param value  the value
         */
        public void set(Object target, Object value);

        /**
         * Has field boolean.
         *
         * @param target the target
         * @return the boolean
         */
        public boolean hasField(Object target);
    }

    // Deduce the net.minecraft.server.v* package
    private static String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
    private static String NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");
    private static String VERSION = OBC_PREFIX.replace("org.bukkit.craftbukkit", "").replace(".", "");

    // Variable replacement
    private static Pattern MATCH_VARIABLE = Pattern.compile("\\{([^\\}]+)\\}");

    private TinyProtocolReflection() {
        // Seal class
    }

    /**
     * Gets field.
     *
     * @param <T>       the type parameter
     * @param target    the target
     * @param name      the name
     * @param fieldType the field type
     * @return the field
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return getField(target, name, fieldType, 0);
    }

    /**
     * Gets field.
     *
     * @param <T>       the type parameter
     * @param className the class name
     * @param name      the name
     * @param fieldType the field type
     * @return the field
     */
    public static <T> FieldAccessor<T> getField(String className, String name, Class<T> fieldType) {
        return getField(getClass(className), name, fieldType, 0);
    }

    /**
     * Gets field.
     *
     * @param <T>       the type parameter
     * @param target    the target
     * @param fieldType the field type
     * @param index     the index
     * @return the field
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType, int index) {
        return getField(target, null, fieldType, index);
    }

    /**
     * Gets field.
     *
     * @param <T>       the type parameter
     * @param className the class name
     * @param fieldType the field type
     * @param index     the index
     * @return the field
     */
    public static <T> FieldAccessor<T> getField(String className, Class<T> fieldType, int index) {
        return getField(getClass(className), fieldType, index);
    }

    // Common method
    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);

                // A function for retrieving a specific field value
                return new FieldAccessor<T>() {

                    @Override
                    @SuppressWarnings("unchecked")
                    public T get(Object target) {
                        try {
                            return (T) field.get(target);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public void set(Object target, Object value) {
                        try {
                            field.set(target, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasField(Object target) {
                        // target instanceof DeclaringClass
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }

        // Search in parent classes
        if (target.getSuperclass() != null)
            return getField(target.getSuperclass(), name, fieldType, index);

        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    /**
     * Gets method.
     *
     * @param className  the class name
     * @param methodName the method name
     * @param params     the params
     * @return the method
     */
    public static MethodInvoker getMethod(String className, String methodName, Class<?>... params) {
        return getTypedMethod(getClass(className), methodName, null, params);
    }

    /**
     * Gets method.
     *
     * @param clazz      the clazz
     * @param methodName the method name
     * @param params     the params
     * @return the method
     */
    public static MethodInvoker getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        return getTypedMethod(clazz, methodName, null, params);
    }

    /**
     * Gets typed method.
     *
     * @param clazz      the clazz
     * @param methodName the method name
     * @param returnType the return type
     * @param params     the params
     * @return the typed method
     */
    public static MethodInvoker getTypedMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... params) {
        for (final Method method : clazz.getDeclaredMethods()) {
            if ((methodName == null || method.getName().equals(methodName))
                    && (returnType == null || method.getReturnType().equals(returnType))
                    && Arrays.equals(method.getParameterTypes(), params)) {
                method.setAccessible(true);

                return new MethodInvoker() {

                    @Override
                    public Object invoke(Object target, Object... arguments) {
                        try {
                            return method.invoke(target, arguments);
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot invoke method " + method, e);
                        }
                    }

                };
            }
        }

        // Search in every superclass
        if (clazz.getSuperclass() != null)
            return getMethod(clazz.getSuperclass(), methodName, params);

        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
    }

    /**
     * Gets constructor.
     *
     * @param className the class name
     * @param params    the params
     * @return the constructor
     */
    public static ConstructorInvoker getConstructor(String className, Class<?>... params) {
        return getConstructor(getClass(className), params);
    }

    /**
     * Gets constructor.
     *
     * @param clazz  the clazz
     * @param params the params
     * @return the constructor
     */
    public static ConstructorInvoker getConstructor(Class<?> clazz, Class<?>... params) {
        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Arrays.equals(constructor.getParameterTypes(), params)) {
                constructor.setAccessible(true);

                return new ConstructorInvoker() {

                    @Override
                    public Object invoke(Object... arguments) {
                        try {
                            return constructor.newInstance(arguments);
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot invoke constructor " + constructor, e);
                        }
                    }

                };
            }
        }

        throw new IllegalStateException(String.format("Unable to find constructor for %s (%s).", clazz, Arrays.asList(params)));
    }

    /**
     * Gets untyped class.
     *
     * @param lookupName the lookup name
     * @return the untyped class
     */
    public static Class<Object> getUntypedClass(String lookupName) {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Class<Object> clazz = (Class) getClass(lookupName);
        return clazz;
    }

    /**
     * Gets class.
     *
     * @param lookupName the lookup name
     * @return the class
     */
    public static Class<?> getClass(String lookupName) {
        return getCanonicalClass(expandVariables(lookupName));
    }

    /**
     * Gets minecraft class.
     *
     * @param name the name
     * @return the minecraft class
     */
    public static Class<?> getMinecraftClass(String name) {
        return getCanonicalClass(NMS_PREFIX + "." + name);
    }

    /**
     * Gets craft bukkit class.
     *
     * @param name the name
     * @return the craft bukkit class
     */
    public static Class<?> getCraftBukkitClass(String name) {
        return getCanonicalClass(OBC_PREFIX + "." + name);
    }

    private static Class<?> getCanonicalClass(String canonicalName) {
        try {
            return Class.forName(canonicalName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find " + canonicalName, e);
        }
    }

    private static String expandVariables(String name) {
        StringBuffer output = new StringBuffer();
        Matcher matcher = MATCH_VARIABLE.matcher(name);

        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement = "";

            // Expand all detected variables
            if ("nms".equalsIgnoreCase(variable))
                replacement = NMS_PREFIX;
            else if ("obc".equalsIgnoreCase(variable))
                replacement = OBC_PREFIX;
            else if ("version".equalsIgnoreCase(variable))
                replacement = VERSION;
            else
                throw new IllegalArgumentException("Unknown variable: " + variable);

            // Assume the expanded variables are all packages, and append a dot
            if (replacement.length() > 0 && matcher.end() < name.length() && name.charAt(matcher.end()) != '.')
                replacement += ".";
            matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(output);
        return output.toString();
    }
}
