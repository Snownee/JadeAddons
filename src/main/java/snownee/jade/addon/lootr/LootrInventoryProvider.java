package snownee.jade.addon.lootr;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import snownee.jade.addon.forge.BlockInventoryProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.view.ItemView;

public enum LootrInventoryProvider implements IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		if (blockEntity instanceof ILootBlockEntity tile) {
			if (tile.getOpeners().contains(player.getUUID())) {
				data.remove("Loot");
				UUID id = tile.getTileId();
				SpecialChestInventory inv = DataStorage.getInventory(level, id, blockEntity.getBlockPos(), player, (RandomizableContainerBlockEntity) blockEntity, tile::unpackLootTable);
				if (inv != null) {
					BlockInventoryProvider.putInvData(data, ItemView.fromContainer(inv, 54, 0));
				}
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return LootrPlugin.INVENTORY;
	}

}
