package me.bimmr.bimmcore.reflection;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A Utilities class to handle reflection
 */
public class Reflection {


    /**
     * Get the server's version
     *
     * @return
     */
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1);
        return version;
    }

    /**
     * Get a craft class
     * (net.minecraft.server)
     *
     * @param name
     * @return
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
     * Get a bukkit class
     * (org.bukkit.craftbukkit)
     *
     * @param name
     * @return
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
     * Get the handle of the passed object
     *
     * @param object
     * @return
     */
    public static Object getHandle(Object object) {
        try {
            return getMethod(object.getClass(), "getHandle").invoke(object, new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a method from the class
     *
     * @param c
     * @param methodName
     * @return
     */
    public static Method getMethod(Class<?> c, String methodName) {
        for (Method method : c.getMethods())
            if (method.getName().equals(methodName))
                return method;
        return null;
    }

    /**
     * Get a method from a class with specific parameter types
     *
     * @param c
     * @param methodName
     * @param paramaterTypes
     * @return
     */
    public static Method getMethod(Class<?> c, String methodName, Class<?>... paramaterTypes) {
        try {
            return c.getMethod(methodName, paramaterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Get the field from the class
     *
     * @param c
     * @param fieldName
     * @return
     */
    public static Field getField(Class<?> c, String fieldName) {
        try {
            return c.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a private field from the class
     *
     * @param c
     * @param fieldName
     * @return
     */
    public static Field getPrivateField(Class<?> c, String fieldName) {
        try {
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
