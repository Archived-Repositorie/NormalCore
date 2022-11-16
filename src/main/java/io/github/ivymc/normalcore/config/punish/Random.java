package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.ivymc.normalcore.PreMain;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Random extends BaseClass {
    java.util.Random random = new java.util.Random();
    private int spreadDistance;
    private int max;
    private boolean setspawnpoint;
    @Override
    public boolean accept(JsonObject json) {
        var spreadDistance = json.get("spreadDistance");
        var max = json.get("max");
        var setspawnpoint = json.get("setspawnpoint");
        if(spreadDistance == null) return false;
        if(max == null) return false;
        if(setspawnpoint == null) return false;
        this.spreadDistance = spreadDistance.getAsInt();
        this.max = max.getAsInt();
        this.setspawnpoint = setspawnpoint.getAsBoolean();
        return super.accept(json);
    }

    @Override
    public void onDeath(ServerPlayerEntity player) {
//        var collection = new ArrayList<ServerPlayerEntity>();
        try {
            spread(player.getServer(), spreadDistance, max, player);
        } catch (Exception err) {
            err.printStackTrace();
        }
        player.setSpawnPoint(player.getWorld().getRegistryKey(), player.getBlockPos(), player.getHeadYaw(), true, false);
    }

    private void spread(MinecraftServer server, float spreadDistance, float maxRange, ServerPlayerEntity player) {
        String command = String.format("spreadplayers ~ ~ %s %s %b @s", spreadDistance, maxRange, false);
        String command_1 = String.format("execute positioned %s %s %s as @p run %s",player.getX(), player.getY(), player.getZ(), command);
        server.getCommandManager().execute(server.getCommandSource(), command_1);
//        double a = center.x - maxRange;
//        double b = center.y - maxRange;
//        double c = center.x + maxRange;
//        double d = center.y + maxRange;
//        SpreadPlayersCommand.Pile[] piles = OpenSpreadPlayers.openMakePiles(random, players.size(), a, b, c, d);
//        OpenSpreadPlayers.openSpread(center, spreadDistance, serverWorld, random, a, b, c, d, maxY, piles, false);
//        var h = OpenSpreadPlayers.openGetMinDistance(players, serverWorld, piles, maxY, false);
    }
}
