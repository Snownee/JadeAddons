package snownee.jadeaddons.create;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.zurrtum.create.content.logistics.tableCloth.TableClothBlockEntity;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum TableClothProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
		return ClientViewGroup.map(list, ItemView::new, null);
	}

	@Override
	public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
		if (!(accessor.getTarget() instanceof TableClothBlockEntity blockEntity)) {
			return null;
		}
		return List.of(new ViewGroup<>(blockEntity.getItemsForRender()));
	}

	@Override
	public Identifier getUid() {
		return CreatePlugin.TABLE_CLOTH;
	}

}
