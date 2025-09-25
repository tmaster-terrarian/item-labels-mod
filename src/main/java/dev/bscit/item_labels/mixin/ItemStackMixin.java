package dev.bscit.item_labels.mixin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.item.tooltip.TooltipType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import dev.bscit.item_labels.components.LabelsComponent;
import dev.bscit.item_labels.components.ItemLabelsComponents;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    @Inject(method = "getTooltip", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", ordinal = 0, target = "net/minecraft/item/ItemStack.appendTooltip (Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V"))
    public void onGetTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, List<Text> list, /*? if <1.21.2 {*/MutableText mutableText,/*?}*/ Consumer<Text> consumer)
    {
        ItemStack thisObject = (ItemStack)(Object)this;

        TooltipAppender tooltipAppender = thisObject.get(ItemLabelsComponents.LABELS);
        if (tooltipAppender != null) {
            tooltipAppender.appendTooltip(context, consumer, type);
        }
    }
}
