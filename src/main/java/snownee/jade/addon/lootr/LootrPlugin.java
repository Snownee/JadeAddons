package snownee.jade.addon.lootr;

import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.lootr.block.LootrBarrelBlock;
import noobanidus.mods.lootr.block.LootrChestBlock;
import noobanidus.mods.lootr.block.LootrInventoryBlock;
import noobanidus.mods.lootr.block.LootrShulkerBlock;
import noobanidus.mods.lootr.block.LootrTrappedChestBlock;

@WailaPlugin(LootrPlugin.ID)
public class LootrPlugin implements IWailaPlugin {
	public static final String ID = "lootr";
	public static final ResourceLocation INFO = new ResourceLocation(ID, "info");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(INFO, true);
		registration.registerBlockDataProvider(LootrInventoryProvider.INSTANCE, RandomizableContainerBlockEntity.class);
		registration.registerBlockDataProvider(LootrInfoProvider.INSTANCE, RandomizableContainerBlockEntity.class);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerComponentProvider(LootrInfoProvider.INSTANCE, TooltipPosition.BODY, LootrBarrelBlock.class);
		registration.registerComponentProvider(LootrInfoProvider.INSTANCE, TooltipPosition.BODY, LootrChestBlock.class);
		registration.registerComponentProvider(LootrInfoProvider.INSTANCE, TooltipPosition.BODY, LootrInventoryBlock.class);
		registration.registerComponentProvider(LootrInfoProvider.INSTANCE, TooltipPosition.BODY, LootrShulkerBlock.class);
		registration.registerComponentProvider(LootrInfoProvider.INSTANCE, TooltipPosition.BODY, LootrTrappedChestBlock.class);
	}

}
