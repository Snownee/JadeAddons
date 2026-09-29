package snownee.jadeaddons.create;

import java.util.List;

import com.zurrtum.create.client.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.zurrtum.create.content.logistics.filter.FilterItem;
import com.zurrtum.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxElement;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.ui.ScreenDirection;

public enum FilterProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public Identifier getUid() {
		return CreatePlugin.FILTER;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!accessor.showDetails() || !(accessor.getBlockEntity() instanceof SmartBlockEntity te)) {
			return;
		}
		FilteringBehaviour<?> behaviour = te.getBehaviour(FilteringBehaviour.TYPE);
		ItemStack filter = behaviour.getFilter(accessor.getSide());
		if (!(filter.getItem() instanceof FilterItem item)) {
			return;
		}
		List<Component> components = item.makeSummary(filter);
		if (components.isEmpty()) {
			return;
		}
		ITooltip tooltip2 = JadeUI.tooltip();
		for (Component component : components) {
			tooltip2.add(JadeUI.text(component).scale(0.5F));
		}
		BoxStyle style = BoxStyle.nestedBox().copy();
		style.borderWidth = 1;
		if (style.padding != null) {
			style.padding[ScreenDirection.UP.ordinal()] = 2;
			style.padding[ScreenDirection.DOWN.ordinal()] = 3;
		}
		BoxElement box = JadeUI.box(tooltip2, style);
		tooltip.add(box);
	}

}
