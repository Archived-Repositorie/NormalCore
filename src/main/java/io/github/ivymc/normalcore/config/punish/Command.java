package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Command implements BaseClass {
    private String command;
    @Override
    public boolean accept(JsonObject json) {
        JsonElement command = json.get("command");
        if(command == null) return false;
        this.command = command.getAsString();
        return true;
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        var server = player.getServer();
        var commandSource = new ServerCommandSource(CommandOutput.DUMMY,player.getPos(),player.getRotationClient(), player.getWorld(),4,"", Text.of("Server"), server,player);
        server.getCommandManager().executeWithPrefix(commandSource,String.format("execute as @p run %s", command));
    }
}
