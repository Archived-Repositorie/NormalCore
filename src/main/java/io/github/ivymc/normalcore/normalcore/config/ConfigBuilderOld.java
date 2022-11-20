package io.github.ivymc.normalcore.normalcore.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ConfigBuilderOld {
    public JsonObject json;
    private static HashMap<String, BaseClass> REGISTRY;
    private ConfigBuilderOld(JsonObject json) {
        this.json = json;
    }

    public static ConfigBuilderOld of(File file, HashMap<String, BaseClass> registry) throws IOException {
        REGISTRY = registry;
        JsonReader reader = new JsonReader(new FileReader(file));
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
        return new ConfigBuilderOld(json);
    }

    public static class Config {
        public boolean enable = false;
        public BaseClass punishmentClass = new BaseClass() {
            @Override
            public boolean accept(JsonObject json) {

                return false;
            }

            @Override
            public void onDeath(ServerPlayerEntity player) {

            }
        };
        public JsonObject punishment = new JsonObject();
        public int lives = 2;
        public boolean cancel = false;
        public boolean forcedrop = false;
    }

    public Config parse() throws Exception {
        Config config = new Config();
        config.enable = json.get("enable").getAsBoolean();
        config.punishmentClass = getPunishment(json.get("punishment").getAsJsonObject());
        config.lives = json.get("lives").getAsInt();
        config.cancel = json.get("cancel").getAsBoolean();
        config.forcedrop = json.get("forcedrop").getAsBoolean();
        return config;
    }

    private BaseClass getPunishment(JsonObject punishment) throws Exception {
        if(punishment == null) throw new Exception("Punishment is null");
        String type = punishment.get("type").getAsString();
        if(type == null) throw new Exception("Punishment type is null");
        punishment.get("afterdeath").getAsBoolean();
        BaseClass clazz = REGISTRY.get(type);
        if(clazz == null) throw new Exception("Punishment type is not registered");
        if(!clazz.accept(punishment)) throw new Exception("Punishment is not valid");
        return clazz;
    }
}
