package snownee.jade.addon.botania;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.block.tile.mana.TilePool;

public enum ManaPoolProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {

	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean showDetails) {
		if (blockEntity instanceof TilePool tile) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("mana", tile.getCurrentMana());
			tag.putInt("manaCap", tile.manaCap);
			data.put("botania", tag);
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(BotaniaPlugin.MANA_POOL) || !accessor.getServerData().contains("botania")) {
			return;
		}
		CompoundTag tag = accessor.getServerData().getCompound("botania");
		int mana = tag.getInt("mana");
		int manaCap = tag.getInt("manaCap");
		tooltip.add(new TextComponent(mana + "/" + manaCap));
	}
}
