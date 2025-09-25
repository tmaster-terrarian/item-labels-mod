package dev.bscit.item_labels.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemLabelsItemTags
{
    public static final TagKey<Item> FORBID_LABELS = create("forbid_labels");
    public static final TagKey<Item> DYES = create("c", "dyes");

    private static TagKey<Item> create(String id)
    {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("item_labels", id));
    }

    private static TagKey<Item> create(String namespace, String id)
    {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, id));
    }
}
