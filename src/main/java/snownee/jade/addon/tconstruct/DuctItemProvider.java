package snownee.jade.addon.tconstruct;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum DuctItemProvider implements IServerExtensionProvider<DuctBlockEntity, ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return TConstructPlugin.DUCT_ITEM;
	}

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
		return List.of();
	}

	@Override
	public List<ViewGroup<ItemStack>> getGroups(ServerPlayer player, ServerLevel level, DuctBlockEntity duct, boolean b) {
		return List.of();
	}
}
