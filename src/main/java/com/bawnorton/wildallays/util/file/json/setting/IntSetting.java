package com.bawnorton.wildallays.util.file.json.setting;

public record IntSetting(Integer value) implements Setting<Integer> {

    public Integer getValue() {
        return value;
    }
}