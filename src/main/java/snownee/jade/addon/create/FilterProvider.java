package snownee.jade.addon.create;

import com.simibubi.create.content.logistics.filter.AttributeFilterMenu.WhitelistMode;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.ItemAttribute;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ITooltipRenderer;
import snownee.jade.impl.ui.ScaledTextElement;

public enum FilterProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.FILTER;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!accessor.showDetails() || !(accessor.getBlockEntity() instanceof SmartBlockEntity)) {
			return;
		}
		SmartBlockEntity te = (SmartBlockEntity) accessor.getBlockEntity();
		FilteringBehaviour behaviour = te.getBehaviour(FilteringBehaviour.TYPE);
		if (behaviour == null) {
			return;
		}
		ItemStack filter = behaviour.getFilter(accessor.getSide());
		if (filter != null && filter.getItem() instanceof FilterItem item) {
			String key = Registry.ITEM.getKey(item).toString();
			IElementHelper elements = IElementHelper.get();
			ITooltip tooltip2 = elements.tooltip();
			if ("create:filter".equals(key)) {
				boolean blacklist = filter.getOrCreateTag().getBoolean("Blacklist");
				boolean respectNBT = filter.getOrCreateTag().getBoolean("RespectNBT");
				var component = blacklist ? Lang.translateDirect("gui.filter.deny_list") : Lang.translateDirect("gui.filter.allow_list");
				component.append("  -  ");
				component.append(respectNBT ? Lang.translateDirect("gui.filter.respect_data") : Lang.translateDirect("gui.filter.ignore_data"));
				tooltip2.add(new ScaledTextElement(component, 0.5F));
				ItemStackHandler filterItems = FilterItem.getFilterItems(filter);
				int count = 0;
				for (int i = 0; i < filterItems.getSlots(); i++) {
					ItemStack filterStack = filterItems.getStackInSlot(i);
					if (filterStack.isEmpty())
						continue;
					IElement element = elements.item(filterStack, 0.8F);
					if (count % 6 == 0) {
						tooltip2.add(element);
					} else {
						tooltip2.append(element);
					}
					count++;
				}
			} else if ("create:attribute_filter".equals(key)) {
				WhitelistMode whitelistMode = WhitelistMode.values()[filter.getOrCreateTag().getInt("WhitelistMode")];
				var component = whitelistMode == WhitelistMode.WHITELIST_CONJ ? Lang.translateDirect("gui.attribute_filter.allow_list_conjunctive") : whitelistMode == WhitelistMode.WHITELIST_DISJ ? Lang.translateDirect("gui.attribute_filter.allow_list_disjunctive") : Lang.translateDirect("gui.attribute_filter.deny_list");
				tooltip2.add(new ScaledTextElement(component, 0.5F));
				int count = 0;
				ListTag attributes = filter.getOrCreateTag().getList("MatchedAttributes", Tag.TAG_COMPOUND);
				for (Tag inbt : attributes) {
					CompoundTag compound = (CompoundTag) inbt;
					ItemAttribute attribute = ItemAttribute.fromNBT(compound);
					boolean inverted = compound.getBoolean("Inverted");
					if (count > 9) {
						tooltip2.add(new ScaledTextElement(Components.literal("- ..."), 0.5F));
						break;
					}
					tooltip2.add(new ScaledTextElement(Components.literal("- ").append(attribute.format(inverted)), 0.5F));
					count++;
				}
			}
			if (!tooltip2.isEmpty()) {
				var style = new BoxStyle();
				style.borderColor = 0x88000000;
				style.bgColor = 0x88000000;
				style.borderWidth = 0.75F;
				var box = elements.box(tooltip2, style);
				box.getTooltipRenderer().setPadding(ITooltipRenderer.TOP, 2);
				box.getTooltipRenderer().setPadding(ITooltipRenderer.BOTTOM, 3);
				tooltip.add(box);
			}
		}
	}

}
