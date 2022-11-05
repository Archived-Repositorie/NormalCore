package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;

public interface BaseClass {
    boolean accept(JsonObject json);
    void onDeath(ServerPlayerEntity player);
}
