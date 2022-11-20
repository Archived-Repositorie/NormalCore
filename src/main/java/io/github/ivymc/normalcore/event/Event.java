package io.github.ivymc.normalcore.event;

import io.github.ivymc.ivycore.events.PlayerEvents;
import io.github.ivymc.normalcore.PreMain;
import io.github.ivymc.normalcore.config.Configs;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import io.github.ivymc.normalcore.config.punish.TempBan;
import io.github.ivymc.normalcore.config.punish.Updating;
import io.github.ivymc.normalcore.helper.PlayerData;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

public class Event {
    static int tick = 0;
    public static void init() {
        PlayerEvents.TICK.register((player, server) -> {
            if(!Configs.PUNISHMENT.data.enable) return;
            if(!(Configs.PUNISHMENT.data.punishment.baseClass instanceof Updating clazz)) return;
            if(!PlayerHelper.of(player).getData().death) return;
            tick++;
            if(tick % clazz.update != 0) return;
            if(!clazz.isDead(player)) {
                clazz.review(player);
                PlayerData data = PlayerHelper.of(player).getData();
                data.death = false;
                PlayerHelper.of(player).writeData(data);
            } else {
                clazz.update(player, false);
            }
        });
        PlayerEvents.JOINED.register((player, server) -> {
            if(!Configs.PUNISHMENT.data.enable) return;
            if(!(Configs.PUNISHMENT.data.punishment.baseClass instanceof Updating clazz)) return;
            if(!clazz.isDead(player)) return;
            clazz.update(player, true);
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if(!Configs.PUNISHMENT.data.enable) return;
            if(!(Configs.PUNISHMENT.data.punishment.baseClass instanceof TempBan clazz)) return;
            if(!clazz.isDead(handler.player)) return;
            clazz.join(handler.player);
        });
        ServerPlayerEvents.AFTER_RESPAWN.register((player, player1, z) -> {
            if(!Configs.PUNISHMENT.data.enable) return;
            BaseClass punishmentClass = Configs.PUNISHMENT.data.punishment.baseClass;
            if(!punishmentClass.getJson().get("afterdeath").getAsBoolean()) return;
            int stat = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS));
            if(stat % Configs.PUNISHMENT.data.lives != 0)  return;
            punishmentClass.onDeath(player);
        });
    }
}
