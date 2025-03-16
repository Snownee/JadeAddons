package snownee.jade.addon.create;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.universal.FluidStorageProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;

public enum HideBoilerHandlerProvider implements IServerExtensionProvider<CompoundTag>,
		IClientExtensionProvider<CompoundTag, FluidView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.HIDE_BOILER_TANKS;
	}

	@Override
	public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
		return FluidStorageProvider.Extension.INSTANCE.getClientGroups(accessor, groups);
	}

	@Override
	public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
		if (!(accessor.getTarget() instanceof FluidTankBlockEntity target)) {
			return null;
		}
		if (target.getControllerBE() != null && target.getControllerBE().boiler.isActive()) {
			return List.of();
		}
		return FluidStorageProvider.Extension.INSTANCE.getGroups(accessor);
	}
}
