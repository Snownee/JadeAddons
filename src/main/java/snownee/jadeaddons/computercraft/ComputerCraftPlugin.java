package snownee.jadeaddons.computercraft;

import dan200.computercraft.shared.computer.blocks.AbstractComputerBlock;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.resources.Identifier;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

public class ComputerCraftPlugin implements IWailaPlugin {
	public static final String ID = "jadeaddons.computercraft";
	public static final Identifier COMPUTER_ID = Identifier.fromNamespaceAndPath(ID, "computer_id");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(ComputerIdProvider.INSTANCE, AbstractComputerBlockEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(ComputerIdProvider.Client.INSTANCE, AbstractComputerBlock.class);
	}
}
