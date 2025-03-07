package snownee.jade.addon.tconstruct;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import snownee.jade.api.Accessor;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ProgressView;
import snownee.jade.api.view.ViewGroup;

public enum CastingTableProvider implements IServerExtensionProvider<CastingBlockEntity, CompoundTag>, IClientExtensionProvider<CompoundTag, ProgressView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return TConstructPlugin.CASTING_TABLE;
	}

	@Override
	public List<ViewGroup<CompoundTag>> getGroups(ServerPlayer player, ServerLevel level, CastingBlockEntity blockEntity, boolean b) {
		if (blockEntity.getCoolingTime() <= 0) {
			return List.of();
		}
		FluidStack fluid = blockEntity.getTank().getFluid();
		if (fluid.isEmpty()) {
			return List.of();
		}
		CompoundTag tag = ProgressView.create((float) blockEntity.getTimer() / blockEntity.getCoolingTime());
		fluid.writeToNBT(tag);
		return List.of(new ViewGroup<>(List.of(tag)));
	}

	@Override
	public List<ClientViewGroup<ProgressView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
		return ClientViewGroup.map(
				groups, tag -> {
					FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag);
					ProgressView view = ProgressView.read(tag);
					view.style.overlay(IElementHelper.get().fluid(JadeFluidObject.of(fluid.getFluid(), fluid.getAmount(), fluid.getTag())));
					return view;
				}, null);
	}
}
