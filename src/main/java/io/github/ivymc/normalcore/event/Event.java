package io.github.ivymc.normalcore.event;

import io.github.ivymc.ivycore.events.PlayerEvents;
import io.github.ivymc.normalcore.PreMain;
import io.github.ivymc.normalcore.config.punish.Updating;
import io.github.ivymc.normalcore.helper.PlayerData;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

public class Event {
    static int tick = 0;
    public static void init() {
        PlayerEvents.TICK.register((player, server) -> {
            if(!PreMain.registry.config.enable) return;
            if(!(PreMain.registry.config.punishmentClass instanceof Updating clazz)) return;
            if(!PlayerHelper.of(player).getData().death) return;
            tick++;
            if(tick % clazz.update != 0) return;
            if(!clazz.isDead(player)) {
                clazz.review(player);
                PlayerData data = PlayerHelper.of(player).getData();
                data.death = false;
                PlayerHelper.of(player).writeData(data);
                player.sendMessageToClient(Text.of("You have been revived!"), false);
            } else {
                clazz.update(player, false);
            }
        });
        PlayerEvents.JOINED.register((player, server) -> {
            if(!PreMain.registry.config.enable) return;
            if(!(PreMain.registry.config.punishmentClass instanceof Updating clazz)) return;
            if(!clazz.isDead(player)) return;
            clazz.update(player, true);
        });
    }
}
