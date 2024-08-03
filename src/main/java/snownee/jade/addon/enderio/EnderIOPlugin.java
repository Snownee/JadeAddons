package snownee.jade.addon.enderio;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class EnderIOPlugin implements IWailaPlugin {
	public static final String ID = "enderio";

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.usePickedResult(BuiltInRegistries.BLOCK.get(new ResourceLocation(ID, "conduit")));
	}
}
