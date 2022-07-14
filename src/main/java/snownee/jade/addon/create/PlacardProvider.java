package snownee.jade.addon.create;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.curiosities.deco.PlacardTileEntity;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;

public enum PlacardProvider implements IComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(CreatePlugin.PLACARD)) {
			return;
		}
		if (accessor.getBlockEntity() instanceof PlacardTileEntity placard) {
			if (!placard.getHeldItem().isEmpty()) {
				tooltip.add(placard.getHeldItem().getHoverName());
			}
		}
	}

	@Override
	public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (config.get(CreatePlugin.PLACARD) && accessor.getBlockEntity() instanceof PlacardTileEntity placard) {
			if (!placard.getHeldItem().isEmpty()) {
				return CreatePlugin.client.getElementHelper().item(placard.getHeldItem());
			}
		}
		return null;
	}

}
