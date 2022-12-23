package io.github.ivymc.normalcore.mixin;

import com.mojang.authlib.GameProfile;
import io.github.ivymc.normalcore.config.Configs;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import io.github.ivymc.normalcore.interfacemixin.IEPlayer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements IEPlayer {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);


    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if(!Configs.PUNISHMENT.data.enable) return;
        BaseClass punishmentClass = Configs.PUNISHMENT.data.punishment.baseClass;
        if(Configs.PUNISHMENT.data.punishment.afterdeath && !Configs.PUNISHMENT.data.cancel) return;
        death(ci, punishmentClass);
    }


    public void death(CallbackInfo ci, BaseClass punishmentClass) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (PlayerHelper.death(punishmentClass, player)) return;
        if(Configs.PUNISHMENT.data.cancel) {
            this.dead = false;
            this.setHealth(this.getMaxHealth());
            ci.cancel();
        }
    }

    @Override
    public void setDeath(boolean death) {
        this.dead = death;
    }

}
