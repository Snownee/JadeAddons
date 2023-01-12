package snownee.jade.addon.create;

import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CopperBacktankProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("Air")) {
			int maxair = BackTankUtil.maxAir(data.getInt("Capacity"));
			tooltip.add(Component.translatable("jadeaddons.create.backtank_air", JadeAddons.seconds(data.getInt("Air")), JadeAddons.seconds(maxair)));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		CopperBacktankTileEntity backtank = (CopperBacktankTileEntity) blockEntity;
		data.putInt("Air", backtank.getAirLevel());
		for (Tag tag : backtank.getEnchantmentTag()) {
			ResourceLocation id = EnchantmentHelper.getEnchantmentId((CompoundTag) tag);
			if ("create:capacity".equals(id.toString())) {
				data.putInt("Capacity", EnchantmentHelper.getEnchantmentLevel((CompoundTag) tag));
				break;
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.COPPER_BACKTANK;
	}

}
