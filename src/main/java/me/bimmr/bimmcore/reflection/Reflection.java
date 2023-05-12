package me.bimmr.bimmcore.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * The type Reflection.
 */
public class Reflection {


    /**
     * Gets version.
     *
     * @return the version
     */
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1);
        return version;
    }

    /**
     * Gets nms class.
     *
     * @param name the name
     * @return the nms class
     */
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets nm class.
     *
     * @param name the name
     * @return the nm class
     */
    public static Class<?> getNMClass(String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets nmw class.
     *
     * @param name the name
     * @return the nmw class
     */
    public static Class<?> getNMWClass(String name) {
        try {
            return Class.forName("net.minecraft.world." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets nbt class.
     *
     * @param name the name
     * @return the nbt class
     */
    public static Class<?> getNBTClass(String name) {
        try {
            return Class.forName("net.minecraft.nbt." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets craft class.
     *
     * @param name the name
     * @return the craft class
     */
    public static Class<?> getCraftClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets class.
     *
     * @param name the name
     * @return the class
     */
    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets handle.
     *
     * @param object the object
     * @return the handle
     */
    public static Object getHandle(Object object) {
        try {
            return getMethod(object.getClass(), "getHandle").invoke(object, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets constructor.
     *
     * @param c     the c
     * @param types the types
     * @return the constructor
     */
    public static Constructor getConstructor(Class<?> c, Class<?>... types) {
        try {
            return c.getConstructor(types);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets method.
     *
     * @param c          the c
     * @param methodName the method name
     * @return the method
     */
    public static Method getMethod(Class<?> c, String methodName) {
        for (Method method : c.getMethods())
            if (method.getName().equals(methodName))
                return method;
        return null;
    }

    /**
     * Gets method.
     *
     * @param c              the c
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @return the method
     */
    public static Method getMethod(Class<?> c, String methodName, Class<?>... parameterTypes) {
        try {
            return c.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Invoke method object.
     *
     * @param c              the c
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @param parameter      the parameter
     * @return the object
     */
    public static Object invokeMethod(Class<?> c, String methodName, Class<?>[] parameterTypes, Object[] parameter) {
        return invokeMethod(c, methodName, null, parameterTypes, parameter);
    }

    /**
     * Invoke method object.
     *
     * @param c          the c
     * @param methodName the method name
     * @param object     the object
     * @return the object
     */
    public static Object invokeMethod(Class<?> c, String methodName, Object object) {
        return invokeMethod(c, methodName, object, null, null);
    }

    /**
     * Invoke method object.
     *
     * @param methodName the method name
     * @param object     the object
     * @return the object
     */
    public static Object invokeMethod(String methodName, Object object) {
        return invokeMethod(object.getClass(), methodName, object, null, null);
    }

    /**
     * Invoke method object.
     *
     * @param c              the c
     * @param methodName     the method name
     * @param object         the object
     * @param parameterTypes the parameter types
     * @param parameter      the parameter
     * @return the object
     */
    public static Object invokeMethod(Class<?> c, String methodName, Object object, Class<?>[] parameterTypes, Object[] parameter) {
        Method m = getMethod(c, methodName, parameterTypes);
        try {
            return m.invoke(object, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Invoke method object.
     *
     * @param method the method
     * @param object the object
     * @param args   the args
     * @return the object
     */
    public static Object invokeMethod(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * New instance object.
     *
     * @param constructor the constructor
     * @param args        the args
     * @return the object
     */
    public static Object newInstance(Constructor constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets field.
     *
     * @param c         the c
     * @param fieldName the field name
     * @param obj       the obj
     * @param value     the value
     */
    public static void setField(Class<?> c, String fieldName, Object obj, Object value) {
        try {
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets field.
     *
     * @param field the field
     * @param obj   the obj
     * @param value the value
     */
    public static void setField(Field field, Object obj, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets field.
     *
     * @param c         the c
     * @param fieldName the field name
     * @return the field
     */
    public static Field getField(Class<?> c, String fieldName) {
        try {
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Get object.
     *
     * @param field  the field
     * @param object the object
     * @return the object
     */
    public static Object get(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get object.
     *
     * @param fieldName the field name
     * @param object    the object
     * @return the object
     */
    public static Object get(String fieldName, Object object) {
        try {
            return get(object.getClass().getDeclaredField(fieldName), object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get object.
     *
     * @param c         the c
     * @param fieldName the field name
     * @param object    the object
     * @return the object
     */
    public static Object get(Class<?> c, String fieldName, Object object) {
        try {
            return get(c.getDeclaredField(fieldName), object);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Get online players player [ ].
     *
     * @return the player [ ]
     */
    public static Player[] getOnlinePlayers() {
        try {
            Collection<? extends Player> p = Bukkit.getOnlinePlayers();
            return p.<Player>toArray(new Player[p.size()]);
        } catch (NoSuchMethodError e) {
            try {
                Player[] players = (Player[]) Reflection.getMethod(Bukkit.class, "getOnlinePlayers").invoke(null, new Object[0]);
                return players;
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e1) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
