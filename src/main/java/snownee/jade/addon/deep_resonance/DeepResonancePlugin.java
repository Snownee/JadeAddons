package snownee.jade.addon.deep_resonance;

import mcjty.deepresonance.modules.core.block.ResonatingCrystalBlock;
import mcjty.deepresonance.modules.core.block.ResonatingCrystalTileEntity;
import mcjty.deepresonance.modules.generator.block.GeneratorPartBlock;
import mcjty.deepresonance.modules.generator.block.GeneratorPartTileEntity;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin(DeepResonancePlugin.ID)
public class DeepResonancePlugin implements IWailaPlugin {
	public static final String ID = "deepresonance";
	public static final ResourceLocation CRYSTAL = new ResourceLocation(ID, "crystal");
	public static final ResourceLocation GENERATOR_PART = new ResourceLocation(ID, "generator_part");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(CRYSTAL, true);
		registration.addConfig(GENERATOR_PART, true);
		registration.registerBlockDataProvider(CrystalProvider.INSTANCE, ResonatingCrystalTileEntity.class);
		registration.registerBlockDataProvider(GeneratorPartProvider.INSTANCE, GeneratorPartTileEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerComponentProvider(CrystalProvider.INSTANCE, TooltipPosition.BODY, ResonatingCrystalBlock.class);
		registration.registerComponentProvider(GeneratorPartProvider.INSTANCE, TooltipPosition.BODY, GeneratorPartBlock.class);
	}

}
