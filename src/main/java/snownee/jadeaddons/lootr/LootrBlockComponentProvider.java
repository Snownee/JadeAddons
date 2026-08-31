package snownee.jadeaddons.lootr;

import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrBlockComponentProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		LootrBlockInfoProvider.INSTANCE.appendTooltip(tooltip, accessor);
	}

	@Override
	public Identifier getUid() {
		return LootrPlugin.INFO;
	}
}
