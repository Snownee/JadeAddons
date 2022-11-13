package snownee.jade.addon.lootr;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrEntityInfoProvider implements IEntityComponentProvider, IServerDataProvider<Entity> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		LootrInfoProvider.appendTooltip(tooltip, accessor);
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, Entity entity, boolean details) {
		LootrInfoProvider.appendServerData(data, entity.getUUID());
	}

	@Override
	public ResourceLocation getUid() {
		return LootrPlugin.INFO;
	}

}
