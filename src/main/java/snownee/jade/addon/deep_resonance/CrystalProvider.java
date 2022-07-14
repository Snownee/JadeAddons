package snownee.jade.addon.deep_resonance;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import mcjty.deepresonance.modules.core.block.ResonatingCrystalTileEntity;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum CrystalProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(DeepResonancePlugin.CRYSTAL) || !accessor.getServerData().contains("DeepResonance")) {
			return;
		}
		CompoundTag tag = accessor.getServerData().getCompound("DeepResonance");
		double strength = tag.getDouble("Strength");
		double efficiency = tag.getDouble("Efficiency");
		double purity = tag.getDouble("Purity");
		int rfpertick = tag.getInt("RfPerTick");
		double power = tag.getDouble("Power");
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		decimalFormat.setRoundingMode(RoundingMode.DOWN);
		tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.sep", decimalFormat.format(strength), decimalFormat.format(efficiency), decimalFormat.format(purity)));
		tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.crystalPower", decimalFormat.format(power), rfpertick));
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean showDetails) {
		if (blockEntity instanceof ResonatingCrystalTileEntity crystal) {
			CompoundTag tag = new CompoundTag();
			tag.putDouble("Strength", crystal.getStrength());
			tag.putDouble("Efficiency", crystal.getEfficiency());
			tag.putDouble("Purity", crystal.getPurity());
			tag.putInt("RfPerTick", crystal.getRfPerTick());
			tag.putDouble("Power", crystal.getPower());
			data.put("DeepResonance", tag);
		}
	}

}
