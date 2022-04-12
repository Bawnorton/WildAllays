package com.bawnorton.wildallays.util.reflect;

import java.lang.reflect.Field;

public class Reflection {
    public static void setField(Object accessed, Object value, String fieldname){
        Field f = getField(accessed, fieldname);
        if (f != null) {
            try {
                if (!f.canAccess(accessed)) {
                    f.setAccessible(true);
                }
                f.set(accessed, value);
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public static Object accessField(Object accessed, String fieldname) {
        Field field = getField(accessed, fieldname);
        if(field != null) {
            if(!field.canAccess(accessed)) {
                field.setAccessible(true);
            }
            try {
                return field.get(Object.class);
            } catch (IllegalAccessException ignored) {}
        }
        return null;
    }

    public static Field getField(Object accessed, String fieldname) {
        Class<?> clazz = accessed instanceof Class<?> ? (Class<?>) accessed : accessed.getClass();
        Field field = null;
        while(clazz.getSuperclass() != null) {
            clazz = clazz.getSuperclass();
            try {
                field = clazz.getDeclaredField(fieldname);
                break;
            } catch (NoSuchFieldException ignored) {}
        }
        return field;
    }
}
