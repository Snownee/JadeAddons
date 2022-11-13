package snownee.jade.addon.lootr;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		appendTooltip(tooltip, accessor);
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		if (blockEntity instanceof ILootBlockEntity tile) {
			appendServerData(data, tile.getTileId());
		}
	}

	public static void appendTooltip(ITooltip tooltip, Accessor<?> accessor) {
		CompoundTag data = accessor.getServerData();
		int decayValue = data.getInt("LootrDecay");
		if (decayValue > 0) {
			tooltip.add(Component.translatable("jadeaddons.lootr.decay", decayValue / 20));
		}
		if (data.getBoolean("LootrRefreshed")) {
			tooltip.add(Component.translatable("jadeaddons.lootr.refreshed"));
		} else {
			int refreshValue = data.getInt("LootrRefresh");
			if (refreshValue > 0) {
				tooltip.add(Component.translatable("jadeaddons.lootr.refresh", refreshValue / 20));
			}
		}
	}

	public static void appendServerData(CompoundTag data, UUID id) {
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

	@Override
	public ResourceLocation getUid() {
		return LootrPlugin.INFO;
	}

}
