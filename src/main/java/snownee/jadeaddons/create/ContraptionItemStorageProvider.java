package snownee.jadeaddons.create;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.jspecify.annotations.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import snownee.jade.addon.universal.ItemCollector;
import snownee.jade.addon.universal.ItemIterator;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum ContraptionItemStorageProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	private final Cache<AbstractContraptionEntity, ItemCollector<Container>> collectors = CacheBuilder.newBuilder().weakKeys().build();

	@Override
	public Identifier getUid() {
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
		try {
			ItemCollector<Container> collector = collectors.get(entity, () -> new ItemCollector<>(new ItemIterator.ContainerItemIterator(
					$ -> Objects.requireNonNull(((AbstractContraptionEntity) Objects.requireNonNull($.getTarget())).getContraption()).getStorage().getAllItems(),
					0)));
			return collector.update(accessor);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
