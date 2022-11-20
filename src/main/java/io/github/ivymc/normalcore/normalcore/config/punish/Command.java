package io.github.ivymc.normalcore.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Command extends BaseClass {
    private String command;
    @Override
    public boolean accept(JsonObject json) {
        JsonElement command = json.get("command");
        if(command == null) return false;
        this.command = command.getAsString();
        return super.accept(json);
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
        var server = player.getServer();
        var commandSource = new ServerCommandSource(CommandOutput.DUMMY,player.getPos(),player.getRotationClient(), player.getWorld(),4,"", Text.of("Server"), server,player);
        server.getCommandManager().execute(commandSource,String.format("execute as @p run %s", command));
    }
}
