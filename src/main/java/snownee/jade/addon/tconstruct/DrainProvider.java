package snownee.jade.addon.tconstruct;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock;
import slimeknights.tconstruct.smeltery.block.entity.component.DrainBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum DrainProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(TConstructPlugin.DRAIN)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		// FluidStorageProvider
		if (!data.contains("JadeFluidStorage")) {
			return;
		}
//		tooltip.remove(VanillaPlugin.FORGE_FLUID);
//		tooltip.remove(VanillaPlugin.INVENTORY);
		FluidStack fluid;
		if (accessor.getBlockEntity() instanceof DuctBlockEntity duct) {
			fluid = duct.getItemHandler().getFluid();
		} else if (accessor.getBlockEntity() instanceof DrainBlockEntity drain) {
			fluid = drain.getDisplayFluid();
		} else {
			return;
		}/*
		JadeFluidObject target = JadeFluidObject.of(fluid.getFluid(), fluid.getAmount(), fluid.getTag());
		for (Tag tag : data.getList("jadeTanks", Tag.TAG_COMPOUND)) {
			FluidStack fluid0 = FluidStack.loadFluidStackFromNBT((CompoundTag) tag);
			if (!fluid0.isEmpty() && matcher.test(fluid0)) {
				target = fluid0;
				break;
			}
		}
		if (target.isEmpty() && accessor.getBlock() instanceof SearedDuctBlock) {
			target = fluid;
		}
		if (!target.isEmpty()) {
			IElementHelper elements = IElementHelper.get();
			tooltip.add(elements.fluid(target));
			tooltip.append(elements.spacer(2, 0));
			tooltip.append(target.getDisplayName());
			IElement amount;
			if (target == fluid) {
				amount = elements.text(Component.translatable("jade.fluid.empty"));
			} else {
				amount = elements.text(Component.literal(IDisplayHelper.get().humanReadableNumber(target.getAmount(), "B", true)));
			}
			amount.size(new Vec2(amount.getSize().x + 18, 2)).translate(new Vec2(18, -7));
			tooltip.add(amount);
		}*/
	}

	@Override
	public ResourceLocation getUid() {
		return TConstructPlugin.DRAIN;
	}
}
