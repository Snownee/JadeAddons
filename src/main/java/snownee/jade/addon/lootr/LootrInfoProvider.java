package snownee.jade.addon.lootr;

import java.util.UUID;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;

public enum LootrInfoProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(LootrPlugin.INFO)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int decayValue = data.getInt("LootrDecay");
		if (decayValue > 0) {
			tooltip.add(new TranslatableComponent("jadeaddons.lootr.decay", decayValue / 20));
		}
		if (data.getBoolean("LootrRefreshed")) {
			tooltip.add(new TranslatableComponent("jadeaddons.lootr.refreshed"));
		} else {
			int refreshValue = data.getInt("LootrRefresh");
			if (refreshValue > 0) {
				tooltip.add(new TranslatableComponent("jadeaddons.lootr.refresh", refreshValue / 20));
			}
		}
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		if (blockEntity instanceof ILootBlockEntity tile) {
			UUID id = tile.getTileId();
			if (!DataStorage.isDecayed(id)) {
				int decayValue = DataStorage.getDecayValue(id);
				if (decayValue > 0) {
					data.putInt("LootrDecay", decayValue);
				}
			}
			if (DataStorage.isRefreshed(id)) {
				data.putBoolean("LootrRefreshed", true);
			} else {
				int refreshValue = DataStorage.getRefreshValue(id);
				if (refreshValue > 0) {
					data.putInt("LootrRefresh", refreshValue);
				}
			}
		}
	}

}
