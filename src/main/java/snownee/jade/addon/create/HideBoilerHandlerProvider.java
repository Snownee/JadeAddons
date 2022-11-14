package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;

import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum HideBoilerHandlerProvider implements IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		FluidTankTileEntity tank = ((FluidTankTileEntity) blockEntity).getControllerTE();
		if (tank == null) {
			return;
		}
		if (tank.boiler.isActive()) {
			data.remove("jadeTanks");
		}
	}

}
