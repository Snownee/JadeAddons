package snownee.jade.addon.lootr;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import noobanidus.mods.lootr.common.block.LootrBarrelBlock;
import noobanidus.mods.lootr.common.block.LootrChestBlock;
import noobanidus.mods.lootr.common.block.LootrInventoryBlock;
import noobanidus.mods.lootr.common.block.LootrShulkerBlock;
import noobanidus.mods.lootr.common.block.LootrTrappedChestBlock;
import noobanidus.mods.lootr.common.entity.LootrChestMinecartEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

public class LootrPlugin implements IWailaPlugin {
	public static final String ID = "lootr";
	public static final ResourceLocation INFO = ResourceLocation.fromNamespaceAndPath(ID, "info");
	public static final ResourceLocation INVENTORY = ResourceLocation.fromNamespaceAndPath(ID, "inventory");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(LootrBlockInfoProvider.INSTANCE, RandomizableContainerBlockEntity.class);
		registration.registerEntityDataProvider(LootrEntityInfoProvider.INSTANCE, LootrChestMinecartEntity.class);

		registration.registerItemStorage(LootrInventoryProvider.INSTANCE, RandomizableContainerBlockEntity.class);
		registration.registerItemStorage(LootrInventoryProvider.INSTANCE, LootrChestMinecartEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(LootrBlockInfoProvider.INSTANCE, LootrBarrelBlock.class);
		registration.registerBlockComponent(LootrBlockInfoProvider.INSTANCE, LootrChestBlock.class);
		registration.registerBlockComponent(LootrBlockInfoProvider.INSTANCE, LootrInventoryBlock.class);
		registration.registerBlockComponent(LootrBlockInfoProvider.INSTANCE, LootrShulkerBlock.class);
		registration.registerBlockComponent(LootrBlockInfoProvider.INSTANCE, LootrTrappedChestBlock.class);
		registration.registerEntityComponent(LootrEntityInfoProvider.INSTANCE, LootrChestMinecartEntity.class);

		registration.registerItemStorageClient(LootrInventoryProvider.INSTANCE);
	}

}
