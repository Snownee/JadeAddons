package snownee.jadeaddons.create;

import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;
import com.zurrtum.create.content.equipment.armor.BacktankUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jadeaddons.mixin.create.BacktankBlockEntityAccess;

public enum BacktankProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("Air")) {
			int maxair = BacktankUtil.maxAir(data.getIntOr("Capacity", 0));
			tooltip.add(Component.translatable(
					"jadeaddons.create.backtank_air",
					IThemeHelper.get().seconds(data.getIntOr("Air", 0), accessor.tickRate()),
					IThemeHelper.get().seconds(maxair, accessor.tickRate())));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof BacktankBlockEntity backtank)) {
			return;
		}
		data.putInt("Air", backtank.getAirLevel());
		data.putInt("Capacity", ((BacktankBlockEntityAccess) backtank).getCapacityEnchantLevel());
	}

	@Override
	public Identifier getUid() {
		return CreatePlugin.BACKTANK_CAPACITY;
	}

}
