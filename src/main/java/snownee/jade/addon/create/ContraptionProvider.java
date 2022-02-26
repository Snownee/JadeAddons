package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;

import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.JadeCommonConfig;
import snownee.jade.addon.forge.InventoryProvider;

public enum ContraptionProvider implements IEntityComponentProvider, IServerDataProvider<Entity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		if (!config.get(CreatePlugin.CONTRAPTION)) {
			return;
		}
		InventoryProvider.append(tooltip, accessor);
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, Entity entity, boolean details) {
		Contraption contraption = ((AbstractContraptionEntity) entity).getContraption();
		int size = details ? JadeCommonConfig.inventoryDetailedShowAmount : JadeCommonConfig.inventoryNormalShowAmount;
		InventoryProvider.putInvData(data, contraption.inventory, size, 0);
	}

}
