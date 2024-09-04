package snownee.jade.addon.create;

import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.mixin.accessor.BacktankBlockEntityAccessor;

public enum BacktankProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("Air")) {
			int maxair = BacktankUtil.maxAir(data.getInt("Capacity"));
			tooltip.add(Component.translatable(
					"jadeaddons.create.backtank_air",
					JadeAddons.seconds(data.getInt("Air")),
					JadeAddons.seconds(maxair)));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BacktankBlockEntity backtank = (BacktankBlockEntity) accessor.getBlockEntity();
		data.putInt("Air", backtank.getAirLevel());
		data.putInt("Capacity", ((BacktankBlockEntityAccessor)backtank).getCapacityEnchantLevel());
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.BACKTANK_CAPACITY;
	}

}
