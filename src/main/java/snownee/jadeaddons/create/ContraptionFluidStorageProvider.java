package snownee.jadeaddons.create;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.resources.Identifier;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.util.JadeForgeUtils;

public enum ContraptionFluidStorageProvider implements IServerExtensionProvider<FluidView.Data>,
		IClientExtensionProvider<FluidView.Data, FluidView> {
	INSTANCE;

	@Override
	public Identifier getUid() {
		return CreatePlugin.CONTRAPTION_FLUIDS;
	}

	@Override
	public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<FluidView.Data>> groups) {
		return ClientViewGroup.map(groups, FluidView::readDefault, null);
	}

	@Override
	public @Nullable List<ViewGroup<FluidView.Data>> getGroups(Accessor<?> accessor) {
		if (!(accessor.getTarget() instanceof AbstractContraptionEntity entity)) {
			return null;
		}
		return JadeForgeUtils.fromFluidHandler(new FluidInventoryResourceHandler(Objects.requireNonNull(entity.getContraption()).getStorage().getFluids()));
	}

}
