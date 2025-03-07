package snownee.jade.addon.tconstruct;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import snownee.jade.api.Accessor;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;

public enum DuctFluidProvider implements IServerExtensionProvider<DuctBlockEntity, CompoundTag>, IClientExtensionProvider<CompoundTag, FluidView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return TConstructPlugin.DUCT_FLUID;
	}

	@Override
	public List<ViewGroup<CompoundTag>> getGroups(ServerPlayer player, ServerLevel level, DuctBlockEntity duct, boolean b) {
		FluidStack fluid = duct.getItemHandler().getFluid();
		if (fluid.isEmpty()) {
			return List.of();
		}
		IFluidHandler fluidHandler = duct.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().orElse(null);
		if (fluidHandler == null) {
			return List.of();
		}
		fluid = fluid.copy();
		fluid.setAmount(Integer.MAX_VALUE);
		FluidStack drained = fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
		int filled = fluidHandler.fill(fluid, IFluidHandler.FluidAction.SIMULATE);
		CompoundTag tag = FluidView.writeDefault(
				JadeFluidObject.of(fluid.getFluid(), drained.getAmount(), fluid.getTag()),
				drained.getAmount() + filled);
		return List.of(new ViewGroup<>(List.of(tag)));
	}

	@Override
	public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> list) {
		return ClientViewGroup.map(list, FluidView::readDefault, null);
	}
}
