package snownee.jade.addon.deep_resonance;

import mcjty.deepresonance.modules.tank.blocks.TankBlock;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin(DeepResonancePlugin.ID)
public class DeepResonancePlugin implements IWailaPlugin {
	public static final String ID = "deepresonance";
	public static final ResourceLocation TANK = new ResourceLocation(ID, "tank");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(TANK, true);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerComponentProvider(TankProvider.INSTANCE, TooltipPosition.BODY, TankBlock.class);
	}

}
