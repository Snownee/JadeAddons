package snownee.jade.addon.create;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ItemViewUtils;
import snownee.jade.api.view.ViewGroup;

public enum ContraptionItemStorageProvider implements IServerExtensionProvider<ItemStack>,
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
	public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
		if (!(accessor.getTarget() instanceof AbstractContraptionEntity entity)) {
			return null;
		}
		Contraption contraption = entity.getContraption();
		var items = contraption.getStorage().getAllItems();
		return ItemViewUtils.groupOf(items, accessor, ignored -> items);
	}
}
