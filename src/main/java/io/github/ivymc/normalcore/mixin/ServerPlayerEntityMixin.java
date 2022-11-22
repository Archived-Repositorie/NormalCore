package io.github.ivymc.normalcore.mixin;

import com.mojang.authlib.GameProfile;
import io.github.ivymc.normalcore.PreMain;
import io.github.ivymc.normalcore.config.Configs;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import io.github.ivymc.normalcore.config.punish.Command;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);


    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if(!Configs.PUNISHMENT.data.enable) return;
        BaseClass punishmentClass = Configs.PUNISHMENT.data.punishment.baseClass;
        if(Configs.PUNISHMENT.data.punishment.afterdeath && !Configs.PUNISHMENT.data.cancel) return;
        death(ci, punishmentClass);
    }

    @Inject(method = "onDeath", at = @At("RETURN"), cancellable = true)
    public void onAfterDeath(DamageSource damageSource, CallbackInfo ci) {
        if(!Configs.PUNISHMENT.data.enable) return;
        BaseClass punishmentClass = Configs.PUNISHMENT.data.punishment.baseClass;
        if(!Configs.PUNISHMENT.data.punishment.afterdeath) return;
        death(ci, punishmentClass);
    }

    public void death(CallbackInfo ci, BaseClass punishmentClass) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if(Configs.PUNISHMENT.data.forcedrop) {
            player.getInventory().dropAll();
        }
        int stat = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS));
        if(stat % Configs.PUNISHMENT.data.lives != 0)  return;
        punishmentClass.onDeath(player);
        if(Configs.PUNISHMENT.data.cancel) {
            this.dead = false;
            this.setHealth(this.getMaxHealth());
            ci.cancel();
        }
    }

}
