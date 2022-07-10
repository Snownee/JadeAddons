package snownee.jade.addon.mcjty_lib;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.tileentity.GenericTileEntity;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.JadeAddons;

@WailaPlugin(McjtyLibPlugin.ID)
public class McjtyLibPlugin implements IWailaPlugin {
	public static final String ID = "mcjtylib";
	public static final ResourceLocation GENERAL = new ResourceLocation(ID, JadeAddons.ID);

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(GENERAL, true);
		registration.registerBlockDataProvider(BaseBlockProvider.INSTANCE, GenericTileEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerComponentProvider(BaseBlockProvider.INSTANCE, TooltipPosition.BODY, BaseBlock.class);
	}

}
