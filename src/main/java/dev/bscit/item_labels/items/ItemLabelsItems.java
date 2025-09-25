package dev.bscit.item_labels.items;

import dev.bscit.item_labels.ItemLabels;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ItemLabelsItems
{
    private static final RegistryKey<Item> LABEL_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ItemLabels.ID, "label"));
    private static final RegistryKey<Item> ERASER_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ItemLabels.ID, "eraser"));

    public static final Item LABEL = new LabelItem(
        new Settings()
            .maxCount(1)
            //? if >=1.21.2
            /*.registryKey(LABEL_KEY)*/
    );

    public static final Item ERASER = new EraserItem(
        new Settings()
            .maxCount(1)
            //? if >=1.21.2
            /*.registryKey(ERASER_KEY)*/
    );

    public static void Initialize()
    {
        Registry.register(Registries.ITEM, LABEL_KEY, LABEL);
        Registry.register(Registries.ITEM, ERASER_KEY, ERASER);
    }
}
