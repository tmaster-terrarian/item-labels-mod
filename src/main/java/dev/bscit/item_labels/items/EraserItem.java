package dev.bscit.item_labels.items;

import dev.bscit.item_labels.components.ItemLabelsComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

public class EraserItem extends Item
{
    public EraserItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player)
    {
        if(clickType != ClickType.RIGHT)
            return false;

        if(otherSlot.getStack().isOf(ItemLabelsItems.LABEL))
            return false;

        if(otherSlot.getStack().remove(ItemLabelsComponents.LABELS) == null)
            return false;

        if(!(player instanceof ServerPlayerEntity))
            player.playSound(SoundEvents.BLOCK_AZALEA_LEAVES_STEP, 0.75f, 1f);

        return true;
    }
}
