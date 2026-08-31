package snownee.jadeaddons.lootr;

import net.minecraft.resources.Identifier;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrEntityComponentProvider implements IEntityComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		LootrEntityInfoProvider.INSTANCE.appendTooltip(tooltip, accessor);
	}

	@Override
	public Identifier getUid() {
		return LootrPlugin.INFO;
	}
}
