package io.github.ivymc.normalcore;

import io.github.ivymc.ivycore.Global;
import io.github.ivymc.normalcore.config.Registry;
import io.github.ivymc.normalcore.event.Event;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import net.fabricmc.api.DedicatedServerModInitializer;

public class Main implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        PlayerHelper.register();
        Event.init();
    }
}
