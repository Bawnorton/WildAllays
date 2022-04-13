package com.bawnorton.wildallays.util.file.json.setting;

import java.util.Map;

public record SettingMap(Map<String, Setting<?>> settings) implements Setting<Map<String, Setting<?>>> {

    @Override
    public Map<String, Setting<?>> getValue() {
        return settings;
    }

}
