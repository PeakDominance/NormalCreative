package me.peakdominance.nc;

import net.minecraft.world.item.CreativeModeTab;

public interface CreativeModeTabBuilderMixinInterface {
    CreativeModeTab.Builder type(CreativeModeTab.Type type);
}
