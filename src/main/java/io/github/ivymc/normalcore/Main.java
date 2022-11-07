package io.github.ivymc.normalcore;

import io.github.ivymc.ivycore.Global;
import io.github.ivymc.normalcore.config.Registry;
import io.github.ivymc.normalcore.event.Event;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        PlayerHelper.register();
        Event.init();
    }
}
