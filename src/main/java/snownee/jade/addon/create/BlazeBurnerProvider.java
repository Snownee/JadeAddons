package snownee.jade.addon.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum BlazeBurnerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag compound = accessor.getServerData();
		FuelType activeFuel = FuelType.NONE;
		boolean isCreative = compound.getBoolean("isCreative");
		if (isCreative) {
			HeatLevel heatLevel = BasinBlockEntity.getHeatLevelOf(accessor.getBlockState());
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
		ItemStack item = new ItemStack(activeFuel == FuelType.SPECIAL ? Items.SOUL_CAMPFIRE : Items.CAMPFIRE);
		tooltip.add(IElementHelper.get().smallItem(item));
		if (isCreative) {
			tooltip.append(Component.translatable("jade.infinity"));
		} else {
			tooltip.append(Component.translatable("jade.seconds", compound.getInt("burnTimeRemaining") / 20));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BlazeBurnerBlockEntity burner = (BlazeBurnerBlockEntity) accessor.getBlockEntity();
		if (burner.isCreative()) {
			data.putBoolean("isCreative", true);
		} else if (burner.getActiveFuel() != FuelType.NONE) {
			data.putInt("fuelLevel", burner.getActiveFuel().ordinal());
			data.putInt("burnTimeRemaining", burner.getRemainingBurnTime());
		}
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.BLAZE_BURNER;
	}

}
