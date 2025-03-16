package snownee.jade.addon.create;

import java.util.List;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.addon.mixin.create.FilterItemAccess;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ScreenDirection;
import snownee.jade.impl.ui.SpecialTextElement;

public enum FilterProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.FILTER;
	}

	@Override
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
		if (filter == null || !(filter.getItem() instanceof FilterItem item)) {
			return;
		}
		List<Component> components = ((FilterItemAccess) item).callMakeSummary(filter);
		if (components.isEmpty()) {
			return;
		}
		IElementHelper elements = IElementHelper.get();
		ITooltip tooltip2 = elements.tooltip();
		for (Component component : components) {
			tooltip2.add(new SpecialTextElement(component).scale(0.5F));
		}
		var box = elements.box(tooltip2, BoxStyle.GradientBorder.DEFAULT_VIEW_GROUP);
		box.setPadding(ScreenDirection.UP, 2);
		box.setPadding(ScreenDirection.DOWN, 3);
		tooltip.add(box);
	}
}
