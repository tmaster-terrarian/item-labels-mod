package dev.bscit.item_labels.components;

import dev.bscit.item_labels.ItemLabels;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

//? if >=1.21.6
/*import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;*/

public class ItemLabelsComponents {
    public static final ComponentType<LabelsComponent> LABELS = ComponentType.<LabelsComponent>builder()
            .codec(LabelsComponent.CODEC)
            .build();

    public static void Initialize()
    {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ItemLabels.ID, "labels"), LABELS);

        //? if >=1.21.6
        /*ComponentTooltipAppenderRegistry.addFirst(LABELS);*/
    }
}
