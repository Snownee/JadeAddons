package snownee.jadeaddons.computercraft;

import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ComputerIdProvider implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof AbstractComputerBlockEntity computer) {
			data.putInt("ComputerId", computer.getComputerID());
		}
	}

	@Override
	public Identifier getUid() {
		return ComputerCraftPlugin.COMPUTER_ID;
	}

	public enum Client implements IBlockComponentProvider {
		INSTANCE;

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			int id = accessor.getServerData().getIntOr("ComputerId", -1);
			if (id >= 0) {
				tooltip.add(Component.translatable("jadeaddons.computercraft.computer_id", id));
			}
		}

		@Override
		public Identifier getUid() {
			return ComputerCraftPlugin.COMPUTER_ID;
		}
	}

}
