package dev.bscit.item_labels.mixin;

import dev.bscit.item_labels.components.ItemLabelsComponents;
import dev.bscit.item_labels.components.LabelsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.component.type.TooltipDisplayComponent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    @Inject(method = "appendTooltip", locals = LocalCapture.NO_CAPTURE, at = @At(value = "HEAD", ordinal = 0))
    public void onAppendTooltip(Item.TooltipContext context, TooltipDisplayComponent displayComponent, PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci)
    {
        ItemStack thisObject = (ItemStack)(Object)this;

        LabelsComponent tooltipAppender = thisObject.get(ItemLabelsComponents.LABELS);
        if (tooltipAppender != null) {
            tooltipAppender.appendTooltip(context, textConsumer, type, null);
        }
    }
}
