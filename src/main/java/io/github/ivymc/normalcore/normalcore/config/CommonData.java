package io.github.ivymc.normalcore.normalcore.config;

import com.google.gson.JsonObject;
import io.github.ivymc.ivycore.config.ConfigData;

public class CommonData extends ConfigData {
    public boolean enable = false;
    @Override
    public void onRead(JsonObject jsonObject) throws Exception {

    }
}
