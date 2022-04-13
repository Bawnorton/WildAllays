package com.bawnorton.wildallays.util.file.json.setting;

public record BooleanSetting(Boolean value) implements Setting<Boolean> {
    public Boolean getValue() {
        return value;
    }
}