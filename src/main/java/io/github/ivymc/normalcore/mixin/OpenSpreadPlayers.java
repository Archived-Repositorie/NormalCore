package io.github.ivymc.normalcore.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.SpreadPlayersCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Collection;
import java.util.Random;
@Mixin(SpreadPlayersCommand.class)
public interface OpenSpreadPlayers {
    @Invoker("spread")
    public static void openSpread(Vec2f center, double spreadDistance, ServerWorld world, Random random, double minX, double minZ, double maxX, double maxZ, int maxY, SpreadPlayersCommand.Pile[] piles, boolean respectTeams) throws CommandSyntaxException {
        throw new AssertionError();
    }
    @Invoker("makePiles")
    public static SpreadPlayersCommand.Pile[] openMakePiles(Random random, int count, double minX, double minZ, double maxX, double maxZ) {
        throw new AssertionError();
    }
    @Invoker("getMinDistance")
    public static double openGetMinDistance(Collection<? extends Entity> entities, ServerWorld world, SpreadPlayersCommand.Pile[] piles, int maxY, boolean respectTeams) {
        throw new AssertionError();
    }
}


