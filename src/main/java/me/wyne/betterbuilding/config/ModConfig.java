package me.wyne.betterbuilding.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "betterbuilding")
@Config.Gui.Background("cloth-config2:transparent")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip()
    public boolean horizontalPlacement = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean verticalPlacement = false;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean renderOutline = true;

}
