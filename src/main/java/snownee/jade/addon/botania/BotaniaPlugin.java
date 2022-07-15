package snownee.jade.addon.botania;

import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.tile.mana.TilePool;

@WailaPlugin(BotaniaPlugin.ID)
public class BotaniaPlugin implements IWailaPlugin {

	public static final String ID = "botania";

	public static final ResourceLocation MANA_POOL = new ResourceLocation(ID, "mana_pool");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(MANA_POOL, true);
		registration.registerBlockDataProvider(ManaPoolProvider.INSTANCE, TilePool.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerComponentProvider(ManaPoolProvider.INSTANCE, TooltipPosition.BODY, BlockPool.class);
	}
}
