package snownee.jadeaddons.lootr;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import noobanidus.mods.lootr.common.block.LootrBarrelBlock;
import noobanidus.mods.lootr.common.block.LootrBrushableBlock;
import noobanidus.mods.lootr.common.block.LootrChestBlock;
import noobanidus.mods.lootr.common.block.LootrCopperChestBlock;
import noobanidus.mods.lootr.common.block.LootrDecoratedPotBlock;
import noobanidus.mods.lootr.common.block.LootrShulkerBoxBlock;
import noobanidus.mods.lootr.common.block.LootrTrappedChestBlock;
import noobanidus.mods.lootr.common.entity.LootrChestMinecartEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

public class LootrPlugin implements IWailaPlugin {
	public static final String ID = "jadeaddons.lootr";
	public static final Identifier INFO = Identifier.fromNamespaceAndPath(ID, "info");
	public static final Identifier INVENTORY = Identifier.fromNamespaceAndPath(ID, "inventory");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(LootrBlockInfoProvider.INSTANCE, RandomizableContainerBlockEntity.class);
		registration.registerEntityDataProvider(LootrEntityInfoProvider.INSTANCE, LootrChestMinecartEntity.class);

		registration.registerItemStorage(LootrInventoryProvider.INSTANCE, RandomizableContainerBlockEntity.class);
		registration.registerItemStorage(LootrInventoryProvider.INSTANCE, LootrChestMinecartEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrBarrelBlock.class);
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrChestBlock.class);
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrCopperChestBlock.class);
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrDecoratedPotBlock.class);
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrBrushableBlock.class);
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrShulkerBoxBlock.class);
		registration.registerBlockComponent(LootrBlockComponentProvider.INSTANCE, LootrTrappedChestBlock.class);
		registration.registerEntityComponent(LootrEntityComponentProvider.INSTANCE, LootrChestMinecartEntity.class);

		registration.registerItemStorageClient(LootrInventoryProvider.INSTANCE);
	}

}
