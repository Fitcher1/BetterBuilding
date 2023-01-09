package me.wyne.betterbuilding.logic;

import me.wyne.betterbuilding.BetterBuilding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class BlockPlacement implements UseItemCallback {

    private final BetterBuilding mod;

    public BlockPlacement(@NotNull final BetterBuilding mod) {
        this.mod = mod;
    }

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (!world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));
        if (!validatePlacement(player)) return TypedActionResult.pass(player.getStackInHand(hand));

        if (!(player.getStackInHand(hand).getItem() instanceof BlockItem))
            return TypedActionResult.fail(player.getStackInHand(hand));

        placeBlock(getBlockHand(player), player, getBlockPlacePosition(world, player, getPlayerLookDirectionInversed(player)));

        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    public Vec3d getPlayerLookDirectionInversed(PlayerEntity player) {
        return player.getEyePos().subtract(mod.getMinecraftClient().crosshairTarget.getPos());
    }

    public BlockHitResult getBlockPlacePosition(World world, PlayerEntity player, Vec3d playerLookDirectionInversed) {
        Vec3d crosshairPosition = mod.getMinecraftClient().crosshairTarget.getPos();

        for (float i = 0.0f; i < 1f; i += 0.05f) {
            BlockHitResult blockPlacePosition = world.raycast(new RaycastContext(crosshairPosition,
                    crosshairPosition.add(playerLookDirectionInversed.multiply(i)),
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player));

            if (checkBlockIntersections(world, blockPlacePosition.getBlockPos())) {
                return blockPlacePosition;
            }
        }

        return null;
    }

    private void placeBlock(Hand playerHand, PlayerEntity player, BlockHitResult blockPlacePosition) {
        if (blockPlacePosition != null) {
            ActionResult blockPlaceResult = mod.getMinecraftClient().interactionManager.interactBlock(mod.getMinecraftClient().player, playerHand, blockPlacePosition);
            if (blockPlaceResult.isAccepted() && blockPlaceResult.shouldSwingHand())
                player.swingHand(playerHand);
        }
    }

    private boolean checkBlockIntersections(World world, BlockPos blockPos) {
        if (!world.getBlockState(blockPos).isAir())
            return false;

        if (mod.getConfig().horizontalPlacement)
            if (world.getBlockState(blockPos.north()).isSolidBlock(world, blockPos.north()) ||
                    world.getBlockState(blockPos.south()).isSolidBlock(world, blockPos.south()) ||
                    world.getBlockState(blockPos.east()).isSolidBlock(world, blockPos.east()) ||
                    world.getBlockState(blockPos.west()).isSolidBlock(world, blockPos.west()))
                return true;
        if (mod.getConfig().verticalPlacement)
            if (world.getBlockState(blockPos.up()).isSolidBlock(world, blockPos.up()) ||
                    world.getBlockState(blockPos.down()).isSolidBlock(world, blockPos.down()))
                return true;

        return false;
    }

    public boolean validatePlacement(PlayerEntity player) {
        if (getBlockHand(player) == null) return false;
        if (mod.getMinecraftClient().crosshairTarget.getType() != HitResult.Type.MISS) return false;
        return true;
    }

    private Hand getBlockHand(PlayerEntity player) {
        for (ItemStack handItem : player.getItemsHand()) {
            if (handItem.getItem() instanceof BlockItem) {
                if (handItem.isItemEqual(player.getMainHandStack())) return Hand.MAIN_HAND;
                else return Hand.OFF_HAND;
            }
        }

        return null;
    }

}
