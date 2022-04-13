package com.bawnorton.wildallays.config.setting;

public interface Setting<E> {
    E getValue();
    default <T> T getValue(Class<T> clazz) {
        return clazz.cast(getValue());
    }
}
