package snownee.jade.addon.create;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.addon.mixin.create.BlueprintOverlayRendererAccess;
import snownee.jade.addon.universal.ItemStorageProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum CraftingBlueprintProvider implements IEntityComponentProvider, IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	public static List<ItemStack> getResults() {
		List<ItemStack> results = BlueprintOverlayRendererAccess.getResults();
		return results == null ? List.of() : results;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (!data.contains("JadeItemStorageUid")) {
			ItemStorageProvider.putData(accessor);
		}
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.CRAFTING_BLUEPRINT;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
		return ClientViewGroup.map(groups, ItemView::new, null);
	}

	@Override
	public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
		return List.of(new ViewGroup<>(getResults()));
	}
}
