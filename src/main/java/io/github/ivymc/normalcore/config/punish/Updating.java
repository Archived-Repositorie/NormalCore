package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ivymc.ivycore.Global;
import io.github.ivymc.normalcore.PreMain;
import io.github.ivymc.normalcore.helper.PlayerData;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import net.minecraft.server.command.TitleCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class Updating {
    protected int time;
    public int update;
    protected Message update_message;
    protected Message death_message;
    public static class Message {
        public String ctx;
        public Boolean overlay;
        private Message(String ctx, Boolean overlay) {
            this.ctx = ctx;
            this.overlay = overlay;
        }
        public static Message parse(JsonObject json) {
            JsonElement ctx = json.get("ctx");
            JsonElement overlay = json.get("overlay");
            if(ctx == null) return null;
            if(overlay == null) return null;
            return new Message(ctx.getAsString(),overlay.getAsBoolean());
        }
    }
    public abstract void review(ServerPlayerEntity player);
    public void update(ServerPlayerEntity player, boolean join) {
        player.sendMessage(Text.Serializer.fromLenientJson(update_message.ctx), update_message.overlay);
    }
    public void onDeath(ServerPlayerEntity player) {
        player.sendMessage(Text.Serializer.fromLenientJson(death_message.ctx), death_message.overlay);
        PlayerData data = PlayerHelper.of(player).getData();
        data.deathEnd = new Date(new Date().getTime() + (time * 1000L));
        data.death = true;
        data = PlayerHelper.of(player).writeData(data);
        PreMain.g.LOGGER.info(data.deathEnd.toString());
        update(player, false);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    public boolean isDead(ServerPlayerEntity player) {
        PlayerData data = PlayerHelper.of(player).getData();
        PreMain.g.LOGGER.info(getDateDiff(new Date(), data.deathEnd, TimeUnit.SECONDS) + "");
        return getDateDiff(new Date(), data.deathEnd, TimeUnit.SECONDS) <= 0;
    }
}
