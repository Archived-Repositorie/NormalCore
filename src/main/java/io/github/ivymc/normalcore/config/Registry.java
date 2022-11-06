package io.github.ivymc.normalcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.ivymc.normalcore.config.punish.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Registry {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public ConfigBuilder.Config config = new ConfigBuilder.Config();

    public static class INIT {
        public static final HashMap<String, BaseClass> REGISTRY = new HashMap<>();
        public static void registerDeath(String id,BaseClass death) {
            REGISTRY.put(id,death);
        }
    }

    public void readConfig() {
        Path pathFile = FabricLoader.getInstance().getConfigDir().resolve("normalcore.json");
        try {
            config = ConfigBuilder.of(pathFile.toFile(), INIT.REGISTRY).parse();
        } catch (IOException e) {
            try {
                config.punishment = new JsonObject();
                config.punishment.addProperty("type","simple");
                Files.writeString(pathFile, GSON.toJson(config));
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        INIT.registerDeath("simple", new SimpleClass());
        INIT.registerDeath("command", new Command());
        INIT.registerDeath("spectator", new Spectator());
        INIT.registerDeath("tempban", new TempBan());
        INIT.registerDeath("updating_command", new UpdatingCommand());
    }
}
