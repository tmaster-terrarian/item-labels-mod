package dev.bscit.item_labels.components;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.bscit.item_labels.ItemLabels;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class LabelsComponent implements TooltipAppender
{
    private static final int MAX_LABELS = 256;

    private static final Text tooltipTemplate = Text.literal("- ").formatted(Formatting.GRAY);
    private static final Text tooltipTitleTemplate = Text.translatable("item_labels.tooltip.labels").append(":").formatted(Formatting.GRAY);

    public static final Codec<LabelsComponent> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.list(TextCodecs.CODEC).fieldOf("labels").forGetter(LabelsComponent::getLabels)
        )
        .apply(instance, LabelsComponent::new)
    );

    private final List<Text> labels;

    private List<Text> getLabels()
    {
        return labels;
    }

    public LabelsComponent(DefaultedList<Text> labels)
    {
        if (labels.size() > MAX_LABELS)
        {
            throw new IllegalArgumentException("Got " + labels.size() + " items, but maximum is " + MAX_LABELS);
        }
        else
        {
            this.labels = Lists.<Text>newArrayList();
            this.labels.addAll(labels);
        }
    }

    private LabelsComponent(int size)
    {
        this(DefaultedList.ofSize(size, Text.empty()));
    }

    private LabelsComponent(List<Text> list)
    {
        this(list.size());

        for (int i = 0; i < list.size(); i++)
        {
            this.labels.set(i, (Text)list.get(i));
        }
    }

    public static LabelsComponent from(List<Text> list)
    {
        LabelsComponent labelsComponent = new LabelsComponent(list.size());

        int i = 0;
        for (Text str : list) {
            labelsComponent.labels.set(i, str);
            i++;
        }

        return labelsComponent;
    }

    public Text last()
    {
        return labels.getLast();
    }

    public boolean isEmpty()
    {
        return labels.isEmpty();
    }

    public ItemStack pop()
    {
        if(isEmpty())
            return null;

        ItemStack stack = new ItemStack(Items.NAME_TAG, 1);
        stack.set(DataComponentTypes.CUSTOM_NAME, last().copyContentOnly());
        labels.removeLast();

        return stack;
    }

    public LabelsComponent withLabel(ItemStack stack)
    {
        labels.add(labels.size(), stack.getName());
        return this;
    }

    public LabelsComponent withLabel(Text text)
    {
        labels.add(labels.size(), text);
        return this;
    }

    public void push(ItemStack stack)
    {
        ItemLabels.LOGGER.info("{}: item {} applied to label", ItemLabels.ID, stack.getName().toString());
        labels.add(labels.size(), stack.getName());
    }

    public void push(Text text)
    {
        labels.add(labels.size(), text);
    }

    public boolean contains(ItemStack stack)
    {
        return labels.contains(stack.getName());
    }

    public boolean contains(Text text)
    {
        return labels.contains(text);
    }

    public boolean containsAll(LabelsComponent other)
    {
        //noinspection SlowListContainsAll
        return labels.containsAll(other.labels);
    }

    public boolean containsAll(Collection<Text> list)
    {
        //noinspection SlowListContainsAll
        return labels.containsAll(list);
    }

    public boolean dyeLast(Formatting formatting)
    {
        MutableText text = labels.getLast().copy();
        labels.set(labels.size() - 1, text.formatted(formatting));
        return true;
    }

    public void undyeLast()
    {
        MutableText text = labels.getLast().copy();
        text.setStyle(Style.EMPTY);
        labels.set(labels.size() - 1, text);
    }

    public static LabelsComponent makeCopy(LabelsComponent labelsComponent)
    {
        LabelsComponent newComponent = new LabelsComponent(labelsComponent.labels.size());

        int i = 0;
        for (Text str : labelsComponent.labels)
        {
            newComponent.labels.set(i, str.copy());
            i++;
        }

        return newComponent;
    }

    public LabelsComponent copyTo(LabelsComponent other)
    {
        for (Text str : labels)
        {
            other.labels.add(other.labels.size(), str.copy());
        }
        return other;
    }

    public boolean equals(Object object)
    {
        return this == object || (object instanceof LabelsComponent labelsComponent && this.labels.equals(labelsComponent.labels));
    }

    public int hashCode()
    {
        return labels.hashCode();
    }

    @Override
    public void appendTooltip(TooltipContext context, Consumer<Text> tooltipConsumer, TooltipType type)
    {
        if(isEmpty())
            return;

        tooltipConsumer.accept(tooltipTitleTemplate.copy());
        for(Text txt : labels)
        {
            TextColor col = txt.getStyle().getColor();
            if(col == TextColor.fromFormatting(Formatting.RESET))
                col = TextColor.fromFormatting(Formatting.YELLOW);
            final TextColor c = col;
            tooltipConsumer.accept(tooltipTemplate.copy().append(txt.copy().styled(style -> style.withColor(c))));
        }
    }
}
