package snownee.jade.addon.mcjty_lib;

import mcjty.lib.api.infusable.CapabilityInfusable;
import mcjty.lib.base.GeneralConfig;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum BaseBlockProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("Infused")) {
			tooltip.add(Component.translatable("jadeaddons.mcjtylib.infused", data.getInt("Infused")));
		}
		if (data.contains("SecurityChannel")) {
			int channel = data.getInt("SecurityChannel");
			String name = data.getString("OwnerName");
			if (channel == -1) {
				tooltip.add(Component.translatable("jadeaddons.mcjtylib.ownedBy", name));
			} else {
				tooltip.add(Component.translatable("jadeaddons.mcjtylib.ownedBy.withChannel", name, channel));
			}
			if (data.getBoolean("OwnerWarning")) {
				tooltip.add(Component.translatable("jadeaddons.mcjtylib.ownedBy.warning").withStyle(ChatFormatting.RED));
			}
		}
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		if (!(blockEntity.getBlockState().getBlock() instanceof BaseBlock)) {
			return;
		}
		blockEntity.getCapability(CapabilityInfusable.INFUSABLE_CAPABILITY).ifPresent(h -> {
			int infused = h.getInfused();
			int pct = infused * 100 / GeneralConfig.maxInfuse.get();
			data.putInt("Infused", pct);
		});
		if (GeneralConfig.manageOwnership.get()) {
			GenericTileEntity generic = (GenericTileEntity) blockEntity;
			if (generic.getOwnerName() != null && !generic.getOwnerName().isEmpty()) {
				data.putInt("SecurityChannel", generic.getSecurityChannel());
				data.putString("OwnerName", generic.getOwnerName());
				if (generic.getOwnerUUID() == null) {
					data.putBoolean("OwnerWarning", true);
				}
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return McjtyLibPlugin.GENERAL;
	}

}
