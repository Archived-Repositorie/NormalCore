package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class BaseClass {
    private JsonObject json = new JsonObject();
    public void accept(JsonObject json) throws Exception {
        this.json = json;
    }
    public abstract void onDeath(ServerPlayerEntity player);
    public JsonObject getJson() {
        return json;
    }
}
