package snownee.jade.addon.deep_resonance;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import mcjty.deepresonance.modules.core.CoreModule;
import mcjty.deepresonance.modules.tank.blocks.TankTileEntity;
import mcjty.deepresonance.modules.tank.data.TankBlob;
import mcjty.deepresonance.util.LiquidCrystalData;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public enum TankProvider implements IComponentProvider {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(DeepResonancePlugin.TANK)) {
			return;
		}
		if (accessor.getBlockEntity() instanceof TankTileEntity tank) {
			TankBlob blob = tank.getBlob();
			if (blob == null) {
				return;
			}
			LiquidCrystalData d = blob.getData();
			FluidStack stack = d.getFluidStack();
			if (stack.getFluid() == CoreModule.LIQUID_CRYSTAL.get()) {
				DecimalFormat decimalFormat = new DecimalFormat("#.#");
				decimalFormat.setRoundingMode(RoundingMode.DOWN);
				tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.quality", decimalFormat.format(d.getQuality() * 100) + "%"));
				tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.efficiency", decimalFormat.format(d.getEfficiency() * 100) + "%"));
				tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.purity", decimalFormat.format(d.getPurity() * 100) + "%"));
				tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.strength", decimalFormat.format(d.getStrength() * 100) + "%"));
			}
		}
	}

}
