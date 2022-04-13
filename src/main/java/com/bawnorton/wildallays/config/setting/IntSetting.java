package com.bawnorton.wildallays.config.setting;

public class IntSetting implements Setting<Integer> {
    public final int max;
    public final int min;
    public final String name;
    private int value;

    public IntSetting(String name, int value, int min, int max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public void setValue(Integer value) {
        this.value = Math.min(Math.max(value, min), max);
    }

    @Override
    public Integer getValue() {
        return value;
    }
}