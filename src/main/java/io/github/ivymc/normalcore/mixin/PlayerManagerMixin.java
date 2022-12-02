package io.github.ivymc.normalcore.mixin;

import io.github.ivymc.normalcore.config.Configs;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import io.github.ivymc.normalcore.interfacemixin.PlayerInterface;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "respawnPlayer", at = @At("HEAD"))
    public void respawnPlayer(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        if(!Configs.PUNISHMENT.data.enable) return;
        BaseClass punishmentClass = Configs.PUNISHMENT.data.punishment.baseClass;
        if(!Configs.PUNISHMENT.data.punishment.afterdeath) return;
        death(cir, punishmentClass, player);
    }

    public void death(CallbackInfoReturnable<ServerPlayerEntity> ci, BaseClass punishmentClass, ServerPlayerEntity player) {
        if (PlayerHelper.death(punishmentClass,player)) return;
        if(Configs.PUNISHMENT.data.cancel) {
            ((PlayerInterface)player).setDeath(false);
            player.setHealth(player.getMaxHealth());
            ci.cancel();
        }
    }
}
