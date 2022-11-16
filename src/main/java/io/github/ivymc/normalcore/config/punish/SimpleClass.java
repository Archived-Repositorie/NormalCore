package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonObject;
import io.github.ivymc.normalcore.PreMain;
import net.minecraft.server.network.ServerPlayerEntity;

public class SimpleClass extends BaseClass {
    @Override
    public boolean accept(JsonObject json) {
        return true;
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
    }
}
