package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MessageUpdating extends Updating{
    protected Message update_message;
    protected Message death_message;
    protected Message review_message;
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

    @Override
    public void accept(JsonObject json) throws Exception {
        JsonElement update_message = json.get("update_message");
        JsonElement death_message = json.get("death_message");
        JsonElement review_message = json.get("review_message");
        if(update_message == null) throw new Exception("update_message field is null");
        if(death_message == null) throw new Exception("death_message field is null");
        if(review_message == null) throw new Exception("review_message field is null");
        this.update_message = Message.parse(update_message.getAsJsonObject());
        if (this.update_message == null) throw new Exception("update_message field is null");
        this.death_message = Message.parse(death_message.getAsJsonObject());
        if (this.death_message == null) throw new Exception("death_message field is null");
        this.review_message = Message.parse(review_message.getAsJsonObject());
        if (this.review_message == null) throw new Exception("review_message field is null");
        super.accept(json);
    }

    @Override
    public void review(ServerPlayerEntity player) {
        player.sendMessage(Text.Serializer.fromLenientJson(review_message.ctx.replaceAll("%time%", getUseTime(player))), review_message.overlay);
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        super.onDeath(player);
        player.sendMessage(Text.Serializer.fromLenientJson(death_message.ctx.replaceAll("%time%", getUseTime(player))), update_message.overlay);
    }

    @Override
    public void update(ServerPlayerEntity player, boolean join) {
        player.sendMessage(Text.Serializer.fromLenientJson(update_message.ctx.replaceAll("%time%", getUseTime(player))), update_message.overlay);
    }
}
