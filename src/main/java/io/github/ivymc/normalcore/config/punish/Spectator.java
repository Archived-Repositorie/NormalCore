package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class Spectator extends MessageUpdating {
    private boolean semi;


    @Override
    public boolean accept(JsonObject json) {
        JsonElement semi = json.get("semi");
        if(semi == null) return false;
        this.semi = semi.getAsBoolean();
        return super.accept(json);
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        super.onDeath(player);
        player.changeGameMode(GameMode.SPECTATOR);
        if(semi) {
            player.changeGameMode(GameMode.ADVENTURE);
            player.setInvisible(true);
            player.setInvulnerable(true);
        }
    }

    @Override
    public void update(ServerPlayerEntity player, boolean join) {
        super.update(player, join);
    }

    @Override
    public void review(ServerPlayerEntity player) {
        super.review(player);
        player.changeGameMode(GameMode.SURVIVAL);
        player.setInvisible(false);
        player.setInvulnerable(false);
    }
}
