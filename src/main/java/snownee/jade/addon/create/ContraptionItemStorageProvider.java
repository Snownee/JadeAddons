package snownee.jade.addon.create;

import java.util.List;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum ContraptionItemStorageProvider implements IServerExtensionProvider<AbstractContraptionEntity, ItemStack>,
		IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.CONTRAPTION_INVENTORY;
	}

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
		return ClientViewGroup.map(groups, ItemView::new, null);
	}

	@Override
	public List<ViewGroup<ItemStack>> getGroups(ServerPlayer player, ServerLevel level, AbstractContraptionEntity entity, boolean showDetails) {
		Contraption contraption = entity.getContraption();
		return List.of(ItemView.fromItemHandler(contraption.getSharedInventory(), 54, 0));
	}

}
