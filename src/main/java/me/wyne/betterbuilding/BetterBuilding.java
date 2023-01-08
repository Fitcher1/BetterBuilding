package me.wyne.betterbuilding;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.wyne.betterbuilding.config.BetterBuildingKeybinds;
import me.wyne.betterbuilding.config.ModConfig;
import me.wyne.betterbuilding.logic.BlockPlacement;
import me.wyne.betterbuilding.visuals.OutlineRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class BetterBuilding implements ModInitializer, ClientModInitializer {

    private final MinecraftClient minecraftClient = MinecraftClient.getInstance();

    private final BlockPlacement blockPlacement = new BlockPlacement(this);
    private final OutlineRenderer outlineRenderer = new OutlineRenderer(this);

    private ModConfig config;
    private BetterBuildingKeybinds keybinds;

    @Override
    public void onInitialize() {

    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        keybinds = new BetterBuildingKeybinds(this);
        UseItemCallback.EVENT.register(blockPlacement);
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(outlineRenderer);
        ClientTickEvents.END_CLIENT_TICK.register(keybinds);
    }

    public MinecraftClient getMinecraftClient() {
        return minecraftClient;
    }

    public BlockPlacement getBlockPlacement() {
        return blockPlacement;
    }

    public ModConfig getConfig() {
        return config;
    }
}
