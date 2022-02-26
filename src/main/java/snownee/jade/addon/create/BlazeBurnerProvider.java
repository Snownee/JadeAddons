package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.processing.BasinTileEntity;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerTileEntity;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerTileEntity.FuelType;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.Jade;

public enum BlazeBurnerProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(CreatePlugin.BLAZE_BURNER)) {
			return;
		}
		CompoundTag compound = accessor.getServerData();
		FuelType activeFuel = FuelType.NONE;
		boolean isCreative = compound.getBoolean("isCreative");
		if (isCreative) {
			HeatLevel heatLevel = BasinTileEntity.getHeatLevelOf(accessor.getBlockState());
			if (heatLevel == HeatLevel.SEETHING) {
				activeFuel = FuelType.SPECIAL;
			} else if (heatLevel != HeatLevel.NONE) {
				activeFuel = FuelType.NORMAL;
			}
		} else {
			activeFuel = FuelType.values()[compound.getInt("fuelLevel")];
		}
		if (activeFuel == FuelType.NONE) {
			return;
		}
		IElementHelper elements = tooltip.getElementHelper();
		ItemStack item = new ItemStack(activeFuel == FuelType.SPECIAL ? Items.SOUL_CAMPFIRE : Items.CAMPFIRE);
		tooltip.add(Jade.smallItem(elements, item));
		if (isCreative) {
			tooltip.append(new TranslatableComponent("jade.infinity"));
		} else {
			tooltip.append(new TranslatableComponent("jade.seconds", compound.getInt("burnTimeRemaining") / 20));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean details) {
		BlazeBurnerTileEntity burner = (BlazeBurnerTileEntity) blockEntity;
		if (burner.isCreative()) {
			data.putBoolean("isCreative", true);
		} else if (burner.getActiveFuel() != FuelType.NONE) {
			data.putInt("fuelLevel", burner.getActiveFuel().ordinal());
			data.putInt("burnTimeRemaining", burner.getRemainingBurnTime());
		}
	}

}
