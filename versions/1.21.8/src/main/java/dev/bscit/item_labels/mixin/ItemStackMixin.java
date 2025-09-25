package dev.bscit.item_labels.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    // no-op for 1.21.6 and onward
}
