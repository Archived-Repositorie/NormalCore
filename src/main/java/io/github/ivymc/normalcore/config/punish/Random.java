package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.ivymc.normalcore.Main;
import io.github.ivymc.normalcore.PreMain;
import io.github.ivymc.normalcore.config.Configs;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

public class Random extends BaseClass {
    java.util.Random random = new java.util.Random();
    private int spreadDistance;
    private int max;
    private boolean setspawnpoint;
    private boolean bedfixed;

    @Override
    public void accept(JsonObject json) throws Exception {
        var spreadDistance = json.get("spreadDistance");
        var max = json.get("max");
        var setspawnpoint = json.get("setspawnpoint");
        var bedfixed = json.get("bedfixed");
        if(spreadDistance == null) throw new Exception("spreadDistance field is null");
        if(max == null) throw new Exception("max field is null");
        if(setspawnpoint == null) throw new Exception("setspawnpoint field is null");
        if(bedfixed == null) throw new Exception("bedfixed field is null");
        this.spreadDistance = spreadDistance.getAsInt();
        this.max = max.getAsInt();
        this.setspawnpoint = setspawnpoint.getAsBoolean();
        this.bedfixed = bedfixed.getAsBoolean();
        super.accept(json);
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
//        var collection = new ArrayList<ServerPlayerEntity>();
        if(bedfixed) {
            var playerRespawnPoint = player.getSpawnPointPosition();
            if(player.getWorld().getBlockState(playerRespawnPoint).isIn(BlockTags.BEDS)) {
                return;
            }
        }
        try {
            spread(player.getServer(), spreadDistance, max, player);
        } catch (Exception err) {
            err.printStackTrace();
        }
        player.setSpawnPoint(player.getWorld().getRegistryKey(), player.getBlockPos(), player.getHeadYaw(), true, false);
    }

    private void spread(MinecraftServer server, float spreadDistance, float maxRange, ServerPlayerEntity player) {
        String command = String.format("spreadplayers ~ ~ %s %s %b @s", spreadDistance, maxRange, false);
        String command_1 = String.format("execute as @p run %s",command);
        var commandSource = new ServerCommandSource(CommandOutput.DUMMY,player.getPos(),player.getRotationClient(), player.getWorld(),4,"", Text.of("Server"), server,player);
        server.getCommandManager().executeWithPrefix(commandSource, command_1);
        if(setspawnpoint) {
            player.setSpawnPoint(player.getWorld().getRegistryKey(), player.getBlockPos(), player.getHeadYaw(), true, false);
        }
//        double a = center.x - maxRange;
//        double b = center.y - maxRange;
//        double c = center.x + maxRange;
//        double d = center.y + maxRange;
//        SpreadPlayersCommand.Pile[] piles = OpenSpreadPlayers.openMakePiles(random, players.size(), a, b, c, d);
//        OpenSpreadPlayers.openSpread(center, spreadDistance, serverWorld, random, a, b, c, d, maxY, piles, false);
//        var h = OpenSpreadPlayers.openGetMinDistance(players, serverWorld, piles, maxY, false);
    }
}
