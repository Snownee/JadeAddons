package snownee.jade.addon.mcjty_lib;

import mcjty.lib.api.infusable.CapabilityInfusable;
import mcjty.lib.base.GeneralConfig;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.tileentity.GenericTileEntity;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum BaseBlockProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(McjtyLibPlugin.GENERAL)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		if (data.contains("Infused")) {
			tooltip.add(new TranslatableComponent("jadeaddons.mcjtylib.infused", data.getInt("Infused")));
		}
		if (data.contains("SecurityChannel")) {
			int channel = data.getInt("SecurityChannel");
			String name = data.getString("OwnerName");
			if (channel == -1) {
				tooltip.add(new TranslatableComponent("jadeaddons.mcjtylib.ownedBy", name));
			} else {
				tooltip.add(new TranslatableComponent("jadeaddons.mcjtylib.ownedBy.withChannel", name, channel));
			}
			if (data.getBoolean("OwnerWarning")) {
				tooltip.add(new TranslatableComponent("jadeaddons.mcjtylib.ownedBy.warning").withStyle(ChatFormatting.RED));
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

}
