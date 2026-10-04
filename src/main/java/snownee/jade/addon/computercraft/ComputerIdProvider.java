package snownee.jade.addon.computercraft;

import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
	public ResourceLocation getUid() {
		return ComputerCraftPlugin.COMPUTER_ID;
	}

	public enum Client implements IBlockComponentProvider {
		INSTANCE;

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			CompoundTag data = accessor.getServerData();
			if (!data.contains("ComputerId")) {
				return;
			}
			int id = data.getInt("ComputerId");
			if (id >= 0) {
				tooltip.add(Component.translatable("jadeaddons.computercraft.computer_id", id));
			}
		}

		@Override
		public ResourceLocation getUid() {
			return ComputerCraftPlugin.COMPUTER_ID;
		}
	}

}
