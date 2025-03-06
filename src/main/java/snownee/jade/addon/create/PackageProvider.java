package snownee.jade.addon.create;

import java.util.List;

import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.box.PackageItem;

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
import snownee.jade.util.JadeForgeUtils;

public enum PackageProvider implements IServerExtensionProvider<PackageEntity, ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
		return ClientViewGroup.map(list, ItemView::new, null);
	}

	@Override
	public List<ViewGroup<ItemStack>> getGroups(
			ServerPlayer player,
			ServerLevel level,
			PackageEntity entity,
			boolean showDetails) {
		return List.of(JadeForgeUtils.fromItemHandler(PackageItem.getContents(entity.box), 54, 0));
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.PACKAGE;
	}
}
