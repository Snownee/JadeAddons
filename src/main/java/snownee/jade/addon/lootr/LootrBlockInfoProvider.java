package snownee.jade.addon.lootr;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrBlockInfoProvider implements IBlockComponentProvider, LootrInfoProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		appendTooltip(tooltip, accessor);
	}
}
