package io.github.ivymc.normalcore.mixin;

import com.mojang.authlib.GameProfile;
import io.github.ivymc.normalcore.PreMain;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if(!PreMain.registry.config.enable) return;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        int stat = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS));;
        if(stat % PreMain.registry.config.lives != 0)  return;
        PreMain.registry.config.punishmentClass.onDeath(player);
        PreMain.g.LOGGER.info(PreMain.registry.config.cancel+"");
        if(PreMain.registry.config.cancel) {
            this.dead = false;
            this.setHealth(this.getMaxHealth());
            ci.cancel();
        }
    }
}
