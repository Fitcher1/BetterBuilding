package me.wyne.betterbuilding.visuals;

import me.wyne.betterbuilding.BetterBuilding;
import me.wyne.betterbuilding.mixin.visuals.DrawBlockOutlineInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class OutlineRenderer implements WorldRenderEvents.BeforeBlockOutline{

    private final BetterBuilding mod;
    private final BlockState blockState;

    public OutlineRenderer(@NotNull final BetterBuilding mod) {
        this.mod = mod;
        blockState = Block.getStateFromRawId(1);
    }

    @Override
    public boolean beforeBlockOutline(WorldRenderContext context, HitResult hitResult) {
        if (!mod.getConfig().renderOutline) return true;
        if (mod.getMinecraftClient().player == null) return true;
        if (!mod.getBlockPlacement().validatePlacement(mod.getMinecraftClient().player)) return true;

        ClientPlayerEntity player = mod.getMinecraftClient().player;
        Vec3d cameraPos = context.camera().getPos();
        BlockHitResult blockPlacePosition = mod.getBlockPlacement().getBlockPlacePosition(mod.getMinecraftClient().world, player, mod.getBlockPlacement().getPlayerLookDirectionInversed(player));

        if (blockPlacePosition != null)
        {
            ((DrawBlockOutlineInvoker)context.worldRenderer()).invokeDrawBlockOutline(context.matrixStack(), context.consumers().getBuffer(RenderLayer.LINES), player,
                    cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(),
                    blockPlacePosition.getBlockPos(), blockState);
        }

        return true;
    }
}