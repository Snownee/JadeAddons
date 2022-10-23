package snownee.jade.addon.mcjty_lib;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(McjtyLibPlugin.ID)
public class McjtyLibPlugin implements IWailaPlugin {
	public static final String ID = "mcjtylib";
	public static final ResourceLocation GENERAL = new ResourceLocation(ID, JadeAddons.ID);

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(BaseBlockProvider.INSTANCE, GenericTileEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(BaseBlockProvider.INSTANCE, BaseBlock.class);
	}

}
