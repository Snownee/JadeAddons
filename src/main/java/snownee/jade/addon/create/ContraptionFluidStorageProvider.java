package snownee.jade.addon.create;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.lootr.LootrPlugin;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.util.JadeForgeUtils;

public enum ContraptionFluidStorageProvider implements IServerExtensionProvider<CompoundTag>,
		IClientExtensionProvider<CompoundTag, FluidView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return LootrPlugin.INVENTORY;
	}

	@Override
	public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
		return ClientViewGroup.map(groups, FluidView::readDefault, null);
	}

	@Override
	public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
		if (!(accessor.getTarget() instanceof AbstractContraptionEntity entity)) {
			return null;
		}
		Contraption contraption = entity.getContraption();
		return JadeForgeUtils.fromFluidHandler(contraption.getStorage().getFluids());
	}
}
