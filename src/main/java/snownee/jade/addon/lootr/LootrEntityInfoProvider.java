package snownee.jade.addon.lootr;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrEntityInfoProvider implements IEntityComponentProvider, LootrInfoProvider<EntityAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		appendTooltip(tooltip, accessor);
	}
}
