package snownee.jade.addon.lootr;

import java.util.UUID;

import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.items.wrapper.InvWrapper;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import snownee.jade.JadeCommonConfig;
import snownee.jade.addon.forge.InventoryProvider;

public enum LootrInventoryProvider implements IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		if (blockEntity instanceof ILootBlockEntity tile) {
			if (tile.getOpeners().contains(player.getUUID())) {
				data.remove("Loot");
				int size = details ? JadeCommonConfig.inventoryDetailedShowAmount : JadeCommonConfig.inventoryNormalShowAmount;
				if (size == 0) {
					return;
				}
				UUID id = tile.getTileId();
				SpecialChestInventory inv = DataStorage.getInventory(level, id, blockEntity.getBlockPos(), player, (RandomizableContainerBlockEntity) blockEntity, tile::unpackLootTable);
				if (inv != null) {
					InventoryProvider.putInvData(data, new InvWrapper(inv), size, 0);
				}
			}
		}
	}

}
