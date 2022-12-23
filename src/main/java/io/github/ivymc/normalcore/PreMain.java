package io.github.ivymc.normalcore;

import io.github.ivymc.ivycore.Global;
import io.github.ivymc.ivycore.config.ConfigBuilder;
import io.github.ivymc.ivycore.registry.RegistryBuilder;
import io.github.ivymc.normalcore.config.CommonData;
import io.github.ivymc.normalcore.config.Configs;
import io.github.ivymc.normalcore.config.PunishmentData;
import io.github.ivymc.normalcore.config.punish.*;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.IOException;
import java.nio.file.Path;

public class PreMain implements PreLaunchEntrypoint {
    public static final Global g = new Global("normalcore");
    public static RegistryBuilder registry = new RegistryBuilder();
    public static ConfigBuilder config = new ConfigBuilder(g.MOD_ID);
    @Override
    public void onPreLaunch() {
        initRegistry();
        try {
            initConfig();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void initRegistry() {
        registry.add(RegistryTypes.PUNISHMENT, g.id("simple"), new SimpleClass());
        registry.add(RegistryTypes.PUNISHMENT, g.id("command"), new Command());
        registry.add(RegistryTypes.PUNISHMENT, g.id("random"), new Random());
        registry.add(RegistryTypes.PUNISHMENT, g.id("updating_command"), new UpdatingCommand());
        registry.add(RegistryTypes.PUNISHMENT, g.id("tempban"), new TempBan());
        registry.add(RegistryTypes.PUNISHMENT, g.id("spectator"), new Spectator());
    }

    private void initConfig() throws Exception {
        config.loadConfig();
        Configs.COMMON = config.createConfigKey(Path.of("common.json"), CommonData.class);
        Configs.PUNISHMENT = config.createConfigKey(Path.of("punishment.json"), PunishmentData.class);
        try {
            Configs.COMMON.readConfig();
            Configs.PUNISHMENT.readConfig();
        } catch (IOException e) {
            try {
                Configs.COMMON.writeConfig();
                Configs.PUNISHMENT.writeConfig();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
