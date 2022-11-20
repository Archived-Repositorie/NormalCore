package io.github.ivymc.normalcore.normalcore.config.punish;

import com.google.gson.JsonObject;
import io.github.ivymc.normalcore.config.punish.MessageUpdating;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TempBan extends MessageUpdating {
    @Override
    public boolean accept(JsonObject json) {
        return super.accept(json);
    }

    @Override
    public void review(ServerPlayerEntity player) {

    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        super.onDeath(player);
        player.networkHandler.disconnect(Text.Serializer.fromLenientJson(death_message.ctx.replaceAll("%time%", getUseTime(player))));
    }

    public void join(ServerPlayerEntity player) {
        player.networkHandler.disconnect(Text.of(update_message.ctx.replaceAll("%time%",  getUseTime(player))));
    }
}
