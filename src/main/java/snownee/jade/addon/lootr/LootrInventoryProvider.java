package snownee.jade.addon.lootr;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.DefaultLootFiller;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import noobanidus.mods.lootr.common.api.data.inventory.ILootrInventory;
import snownee.jade.addon.universal.ItemCollector;
import snownee.jade.addon.universal.ItemIterator;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum LootrInventoryProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return LootrPlugin.INVENTORY;
	}

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
		return ClientViewGroup.map(groups, ItemView::new, null);
	}

	@Override
	public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
		Object target = accessor.getTarget();
		Player player = accessor.getPlayer();
		if (target instanceof ILootrInfoProvider infoProvider && infoProvider.hasOpened(player)) {
			ILootrInventory inventory = LootrAPI.getInventory(
					infoProvider,
					((ServerPlayer) accessor.getPlayer()),
					DefaultLootFiller.getInstance());
			if (inventory != null) {
				return new ItemCollector<>(new ItemIterator.ContainerItemIterator($ -> inventory, 0)).update(accessor);
			}
		}
		return null;
	}
}
