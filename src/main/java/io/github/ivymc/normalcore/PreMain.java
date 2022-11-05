package io.github.ivymc.normalcore;

import io.github.ivymc.ivycore.Global;
import io.github.ivymc.normalcore.config.Registry;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreMain implements PreLaunchEntrypoint {
    public static final Global g = new Global("normalcore");
    public static Registry registry = new Registry();
    @Override
    public void onPreLaunch() {
        registry.init();
        registry.readConfig();
    }
}
