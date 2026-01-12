package me.peakdominance.nc.mixin;

import me.peakdominance.nc.CreativeModeTabBuilderMixinInterface;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreativeModeTab.Builder.class)
public class CreativeModeTabBuilderMixin implements CreativeModeTabBuilderMixinInterface {
    @Shadow
    private CreativeModeTab.Type type;

    /**
     * @author Matthew
     * @reason typing freedom!
     */
    @Overwrite
    public CreativeModeTab.Builder type(CreativeModeTab.Type type) {
        this.type = type;
        return ((CreativeModeTab.Builder)(Object)this);
    }
}
