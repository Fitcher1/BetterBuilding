package me.wyne.betterbuilding.config;

import me.wyne.betterbuilding.BetterBuilding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BetterBuildingKeybinds implements ClientTickEvents.EndTick {

    private final BetterBuilding mod;

    private final KeyBinding toggleHorizontalPlacement;
    private final KeyBinding toggleVerticalPlacement;
    private final KeyBinding toggleOutlineRendering;

    public BetterBuildingKeybinds(@NotNull final BetterBuilding mod)
    {
        this.mod = mod;

        toggleHorizontalPlacement = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterbuilding.toggleHorizontalPlacement",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.betterbuilding.header"
        ));

        toggleVerticalPlacement = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterbuilding.toggleVerticalPlacement",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.betterbuilding.header"
        ));

        toggleOutlineRendering = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterbuilding.toggleOutlineRendering",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_COMMA,
                "category.betterbuilding.header"
        ));
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        while (toggleHorizontalPlacement.wasPressed()) {
            mod.getConfig().horizontalPlacement = !mod.getConfig().horizontalPlacement;
            if (mod.getConfig().horizontalPlacement)
                client.player.sendMessage(Text.literal("Horizontal placement: ").append("ON").formatted(Formatting.GREEN), true);
            else
                client.player.sendMessage(Text.literal("Horizontal placement: ").append("OFF").formatted(Formatting.RED), true);
        }
        while (toggleVerticalPlacement.wasPressed()) {
            mod.getConfig().verticalPlacement = !mod.getConfig().verticalPlacement;
            if (mod.getConfig().verticalPlacement)
                client.player.sendMessage(Text.literal("Vertical placement: ").append("ON").formatted(Formatting.GREEN), true);
            else
                client.player.sendMessage(Text.literal("Vertical placement: ").append("OFF").formatted(Formatting.RED), true);
        }
        while (toggleOutlineRendering.wasPressed()) {
            mod.getConfig().renderOutline = !mod.getConfig().renderOutline;
            if (mod.getConfig().renderOutline)
                client.player.sendMessage(Text.literal("Outline rendering: ").append("ON").formatted(Formatting.GREEN), true);
            else
                client.player.sendMessage(Text.literal("Outline rendering: ").append("OFF").formatted(Formatting.RED), true);
        }
    }

}
