package com.bawnorton.wildallays.config.setting;

public class BooleanSetting implements Setting<Boolean> {
    public final String name;
    private boolean value;

    public BooleanSetting(String name, Boolean value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}