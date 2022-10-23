package snownee.jade.addon.lootr;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.lootr.block.LootrBarrelBlock;
import noobanidus.mods.lootr.block.LootrChestBlock;
import noobanidus.mods.lootr.block.LootrInventoryBlock;
import noobanidus.mods.lootr.block.LootrShulkerBlock;
import noobanidus.mods.lootr.block.LootrTrappedChestBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(LootrPlugin.ID)
public class LootrPlugin implements IWailaPlugin {
	public static final String ID = "lootr";
	public static final ResourceLocation INFO = new ResourceLocation(ID, "info");
	public static final ResourceLocation INVENTORY = new ResourceLocation(ID, "inventory");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(LootrInventoryProvider.INSTANCE, RandomizableContainerBlockEntity.class);
		registration.registerBlockDataProvider(LootrInfoProvider.INSTANCE, RandomizableContainerBlockEntity.class);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(LootrInfoProvider.INSTANCE, LootrBarrelBlock.class);
		registration.registerBlockComponent(LootrInfoProvider.INSTANCE, LootrChestBlock.class);
		registration.registerBlockComponent(LootrInfoProvider.INSTANCE, LootrInventoryBlock.class);
		registration.registerBlockComponent(LootrInfoProvider.INSTANCE, LootrShulkerBlock.class);
		registration.registerBlockComponent(LootrInfoProvider.INSTANCE, LootrTrappedChestBlock.class);
	}

}
