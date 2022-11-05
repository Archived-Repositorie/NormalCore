package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class Spectator extends Updating implements BaseClass {
    private boolean semi;


    @Override
    public boolean accept(JsonObject json) {
        JsonElement time = json.get("time");
        JsonElement semi = json.get("semi");
        JsonElement update = json.get("update");
        JsonElement update_message = json.get("update_message");
        JsonElement death_message = json.get("death_message");
        if(time == null) return false;
        if(semi == null) return false;
        if(update == null) return false;
        if(update_message == null) return false;
        if(death_message == null) return false;
        this.time = time.getAsInt();
        this.semi = semi.getAsBoolean();
        this.update = update.getAsInt();
        this.update_message = Message.parse(update_message.getAsJsonObject());
        if (this.update_message == null) return false;
        this.death_message = Message.parse(death_message.getAsJsonObject());
        if (this.death_message == null) return false;
        return true;
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        super.onDeath(player);
        player.changeGameMode(GameMode.SPECTATOR);
        if(semi) {
            player.changeGameMode(GameMode.CREATIVE);
        }
    }

    @Override
    public void update(ServerPlayerEntity player, boolean join) {
        super.update(player, join);
        if(join) player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_MODE_CHANGED, (float)GameMode.ADVENTURE.getId()));
    }

    @Override
    public void review(ServerPlayerEntity player) {
        player.changeGameMode(GameMode.SURVIVAL);
    }
}
