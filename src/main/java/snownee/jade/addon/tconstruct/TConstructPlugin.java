/*package snownee.jade.addon.tconstruct;

import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.smeltery.block.AbstractCastingBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDrainBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock;

@WailaPlugin(TConstructPlugin.ID)
public class TConstructPlugin implements IWailaPlugin {
	public static final String ID = "tconstruct";
	public static final ResourceLocation CASTING_TABLE = new ResourceLocation(ID, "casting_table");
	public static final ResourceLocation DRAIN = new ResourceLocation(ID, "drain");
	static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(CASTING_TABLE, true);
		registration.addConfig(DRAIN, true);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.registerComponentProvider(CastingTableProvider.INSTANCE, TooltipPosition.BODY, AbstractCastingBlock.class);
		registration.registerComponentProvider(DrainProvider.INSTANCE, TooltipPosition.BODY, SearedDuctBlock.class);
		registration.registerComponentProvider(DrainProvider.INSTANCE, TooltipPosition.BODY, SearedDrainBlock.class);
	}

}
*/