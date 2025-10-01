package dev.bscit.item_labels.items;

import dev.bscit.item_labels.components.LabelsComponent;
import dev.bscit.item_labels.components.ItemLabelsComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
/*? if <1.21.2 {*/
import net.minecraft.util.TypedActionResult;
/*?} else {*/
/*import net.minecraft.util.ActionResult;
*//*?}*/
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LabelItem extends Item
{
    public LabelItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player)
    {
        if(clickType != ClickType.RIGHT)
            return false;

        LabelsComponent labels = thisStack.get(ItemLabelsComponents.LABELS);

        if(otherSlot.getStack().isOf(Items.NAME_TAG))
        {
            if(labels == null)
                labels = new LabelsComponent(DefaultedList.ofSize(0, Text.empty()));
            // absorb name tag
            labels.push(otherSlot.takeStack(1));
            if(!(player instanceof ServerPlayerEntity))
                player.playSound(SoundEvents.ITEM_BUNDLE_INSERT);
            otherSlot.markDirty();
            thisStack.set(ItemLabelsComponents.LABELS, labels);
            return true;
        }

        if(labels == null || labels.isEmpty())
            return false;

        if(!otherSlot.hasStack())
        {
            // remove label
            ItemStack toInsert = labels.pop();
            if(!otherSlot.canInsert(toInsert))
                return false;

            if(!(player instanceof ServerPlayerEntity))
                player.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE);

            otherSlot.insertStack(toInsert);
            otherSlot.markDirty();
            if(labels.isEmpty())
                thisStack.remove(ItemLabelsComponents.LABELS);
            else
                thisStack.set(ItemLabelsComponents.LABELS, labels);
            return true;
        }
        else if(!otherSlot.getStack().isIn(ItemLabelsItemTags.FORBID_LABELS))
        {
            // assign label to otherSlot
            LabelsComponent otherLabels = otherSlot.getStack().get(ItemLabelsComponents.LABELS);
            if(otherLabels == null)
            {
                otherSlot.getStack().set(ItemLabelsComponents.LABELS, LabelsComponent.makeCopy(labels));
                return true;
            }

            if(!otherLabels.containsAll(labels))
            {
                otherSlot.getStack().set(ItemLabelsComponents.LABELS, labels.copyTo(otherLabels));
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
    {
        if(clickType != ClickType.RIGHT)
            return false;
        if(otherStack.isEmpty())
            return false;

        LabelsComponent labels = thisStack.get(ItemLabelsComponents.LABELS);
        if(labels == null)
        {
            thisStack.set(ItemLabelsComponents.LABELS, new LabelsComponent(DefaultedList.ofSize(0, Text.empty())));
            labels = thisStack.get(ItemLabelsComponents.LABELS);
        }

        if(otherStack.isOf(Items.NAME_TAG))
        {
            // absorb name tag
            labels.push(otherStack);
            if(!(player instanceof ServerPlayerEntity))
                player.playSound(SoundEvents.ITEM_BUNDLE_INSERT);
            cursorStackReference.get().decrement(1);
            thisStack.set(ItemLabelsComponents.LABELS, labels);
            return true;
        }
        else if(otherStack.isIn(ItemLabelsItemTags.DYES) && !labels.isEmpty())
        {
            Formatting color = getColor(otherStack);

            if(labels.dyeLast(color))
            {
                cursorStackReference.get().decrement(1);
                return true;
            }
        }

        return false;
    }

    private static @Nullable Formatting getColor(ItemStack otherStack) {
        int dye = -1;
        if(otherStack.isOf(Items.WHITE_DYE)) dye = 0;
        if(otherStack.isOf(Items.ORANGE_DYE)) dye = 1;
        if(otherStack.isOf(Items.MAGENTA_DYE)) dye = 2;
        if(otherStack.isOf(Items.LIGHT_BLUE_DYE)) dye = 3;
        if(otherStack.isOf(Items.YELLOW_DYE)) dye = 4;
        if(otherStack.isOf(Items.LIME_DYE)) dye = 5;
        if(otherStack.isOf(Items.PINK_DYE)) dye = 6;
        if(otherStack.isOf(Items.GRAY_DYE)) dye = 7;
        if(otherStack.isOf(Items.LIGHT_GRAY_DYE)) dye = 8;
        if(otherStack.isOf(Items.CYAN_DYE)) dye = 9;
        if(otherStack.isOf(Items.PURPLE_DYE)) dye = 10;
        if(otherStack.isOf(Items.BLUE_DYE)) dye = 11;
        if(otherStack.isOf(Items.BROWN_DYE)) dye = 12;
        if(otherStack.isOf(Items.GREEN_DYE)) dye = 13;
        if(otherStack.isOf(Items.RED_DYE)) dye = 14;
        if(otherStack.isOf(Items.BLACK_DYE)) dye = 15;
        int id = dyeToFormat(dye);
        Formatting color = Formatting.RESET;
        if(id == -1)
            color = Formatting.BOLD;
        else if(id == -2)
            color = Formatting.UNDERLINE;
        else
            color = Formatting.byColorIndex(id);
        return color;
    }

    private static int dyeToFormat(int dye)
    {
        return switch (dye) {
            case 0 -> 15;
            case 1 -> 6;
            case 2 -> 5;
            case 3 -> 11;
            case 4 -> 14;
            case 5 -> 10;
            case 6 -> 13;
            case 7 -> 8;
            case 8 -> 7;
            case 9 -> 3;
            case 10 -> -1;
            case 11 -> 1;
            case 12 -> -2;
            case 13 -> 2;
            case 14 -> 12;
            case 15 -> 0;
            default -> 14;
        };
    }

    @Override
    /*? if <1.21.2 {*/
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    /*?} else {*/
    /*public ActionResult use(World world, PlayerEntity user, Hand hand)
    *//*?}*/
    {
        ItemStack itemStack = user.getStackInHand(hand);

        LabelsComponent labels = itemStack.get(ItemLabelsComponents.LABELS);

        if(labels == null || labels.isEmpty())
            return super.use(world, user, hand);

        user.setCurrentHand(hand);

        while(!labels.isEmpty())
            user.getInventory().offerOrDrop(labels.pop());

        /*? if <1.21.9 {*/
        if(world.isClient)
            user.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS);
        /*?} else {*/
        /*if(world.isClient())
            user.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS);
        *//*?}*/

        itemStack.remove(ItemLabelsComponents.LABELS);

        /*? if <1.21.2 {*/
        return TypedActionResult.success(itemStack);
        /*?} else {*/
        /*return ActionResult.SUCCESS;
        *//*?}*/
    }
}
