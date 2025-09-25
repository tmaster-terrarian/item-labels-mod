package dev.bscit.item_labels.items;

import dev.bscit.item_labels.ItemLabels;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemLabelsItems
{
    public static final Item LABEL = new LabelItem(
        new Settings()
            .maxCount(1)
    );

    public static final Item ERASER = new EraserItem(
        new Settings()
            .maxCount(1)
    );

    public static void Initialize()
    {
        Registry.register(Registries.ITEM, Identifier.of(ItemLabels.ID, "label"), LABEL);
        Registry.register(Registries.ITEM, Identifier.of(ItemLabels.ID, "eraser"), ERASER);
    }
}
