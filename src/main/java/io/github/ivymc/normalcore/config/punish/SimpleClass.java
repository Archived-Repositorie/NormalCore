package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;

public class SimpleClass extends BaseClass {
    @Override
    public void accept(JsonObject json) {
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
    }
}
