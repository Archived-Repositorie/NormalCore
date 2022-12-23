package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class UpdatingCommand extends Updating {
    private String update_command;
    private String review_command;
    private String death_command;

    @Override
    public void accept(JsonObject json) throws Exception {
        JsonElement update_command = json.get("update_command");
        JsonElement review_command = json.get("review_command");
        JsonElement death_command = json.get("death_command");
        if (update_command == null) throw new Exception("update_command field is null");
        if (review_command == null) throw new Exception("review_command field is null");
        if (death_command == null) throw new Exception("death_command field is null");
        this.update_command = update_command.getAsString();
        this.review_command = review_command.getAsString();
        this.death_command = death_command.getAsString();
        super.accept(json);
    }

    @Override
    public void review(ServerPlayerEntity player) {
        var server = player.getServer();
        var commandSource = new ServerCommandSource(CommandOutput.DUMMY, player.getPos(), player.getRotationClient(), player.getWorld(), 4, "", Text.of("Server"), server, player);
        server.getCommandManager().execute(commandSource, String.format("execute as @p run %s", review_command.replaceAll("%time%",getUseTime(player))));
    }

    @Override
    public void update(ServerPlayerEntity player, boolean join) {
        var server = player.getServer();
        var commandSource = new ServerCommandSource(CommandOutput.DUMMY, player.getPos(), player.getRotationClient(), player.getWorld(), 4, "", Text.of("Server"), server, player);
        server.getCommandManager().execute(commandSource, String.format("execute as @p run %s", update_command.replaceAll("%time%", getUseTime(player))));
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        super.onDeath(player);
        var server = player.getServer();
        var commandSource = new ServerCommandSource(CommandOutput.DUMMY, player.getPos(), player.getRotationClient(), player.getWorld(), 4, "", Text.of("Server"), server, player);
        server.getCommandManager().execute(commandSource, String.format("execute as @p run %s", death_command.replaceAll("%time%", getUseTime(player))));
    }
}
