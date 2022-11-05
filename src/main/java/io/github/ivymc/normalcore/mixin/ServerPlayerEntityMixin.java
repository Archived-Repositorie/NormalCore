package io.github.ivymc.normalcore.mixin;

import io.github.ivymc.normalcore.PreMain;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if(!PreMain.registry.config.enable) return;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        int stat = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS));;
        if(stat % PreMain.registry.config.lives != 0)  return;
        PreMain.registry.config.punishmentClass.onDeath(player);
        if(PreMain.registry.config.cancel) ci.cancel();
    }
}
