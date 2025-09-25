package dev.bscit.item_labels;

import dev.bscit.item_labels.components.ItemLabelsComponents;
import dev.bscit.item_labels.items.ItemLabelsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemLabels implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Label My Items");
    public static final String ID = "item_labels";

    @Override
    public void onInitialize()
    {
        LOGGER.info("salutations from {}! stay hydrated!", ID);

        LOGGER.info("{}: registering component types", ID);
        ItemLabelsComponents.Initialize();

        LOGGER.info("{}: registering items", ID);
        ItemLabelsItems.Initialize();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.addAfter(Items.NAME_TAG, ItemLabelsItems.LABEL);
            content.addAfter(ItemLabelsItems.LABEL, ItemLabelsItems.ERASER);
        });
    }
}
