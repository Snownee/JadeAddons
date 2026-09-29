package snownee.jadeaddons.create;

import com.zurrtum.create.content.processing.basin.BasinBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.JadeUI;

public enum BlazeBurnerProvider implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof BlazeBurnerBlockEntity burner)) {
			return;
		}
		if (burner.isCreative()) {
			data.putBoolean("isCreative", true);
		} else if (burner.getActiveFuel() != FuelType.NONE) {
			data.putInt("fuelLevel", burner.getActiveFuel().ordinal());
			data.putInt("burnTimeRemaining", burner.getRemainingBurnTime());
		}
	}

	@Override
	public Identifier getUid() {
		return CreatePlugin.BLAZE_BURNER;
	}

	public enum Client implements IBlockComponentProvider {
		INSTANCE;

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			CompoundTag compound = accessor.getServerData();
			FuelType activeFuel = FuelType.NONE;
			boolean isCreative = compound.getBooleanOr("isCreative", false);
			if (isCreative) {
				HeatLevel heatLevel = BasinBlockEntity.getHeatLevelOf(accessor.getBlockState());
				if (heatLevel == HeatLevel.SEETHING) {
					activeFuel = FuelType.SPECIAL;
				} else if (heatLevel != HeatLevel.NONE) {
					activeFuel = FuelType.NORMAL;
				}
			} else {
				activeFuel = FuelType.values()[compound.getIntOr("fuelLevel", 0)];
			}
			if (activeFuel == FuelType.NONE) {
				return;
			}
			ItemStack item = new ItemStack(activeFuel == FuelType.SPECIAL ? Items.SOUL_CAMPFIRE : Items.CAMPFIRE);
			tooltip.add(JadeUI.smallItem(item));
			if (isCreative) {
				tooltip.append(IThemeHelper.get().info(Component.translatable("jade.infinity")));
			} else {
				tooltip.append(IThemeHelper.get().seconds(compound.getIntOr("burnTimeRemaining", 0), accessor.tickRate()));
			}
		}

		@Override
		public Identifier getUid() {
			return CreatePlugin.BLAZE_BURNER;
		}
	}

}
