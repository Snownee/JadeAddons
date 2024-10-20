/*package snownee.jade.addon.tconstruct;

import java.util.function.Predicate;

import com.google.common.base.Predicates;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import mcp.mobius.waila.api.ui.IElementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import snownee.jade.VanillaPlugin;

public enum DrainProvider implements IComponentProvider {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(TConstructPlugin.DRAIN) || !accessor.getServerData().contains("jadeTanks")) {
			return;
		}
		tooltip.remove(VanillaPlugin.FORGE_FLUID);
		tooltip.remove(VanillaPlugin.INVENTORY);
		Predicate<FluidStack> matcher;
		FluidStack fluid = FluidStack.EMPTY;
		if (accessor.getBlock() instanceof SearedDuctBlock) {
			if (!(accessor.getBlockEntity() instanceof DuctBlockEntity)) {
				return;
			}
			DuctBlockEntity duct = (DuctBlockEntity) accessor.getBlockEntity();
			fluid = duct.getItemHandler().getFluid();
			matcher = fluid::isFluidEqual;
		} else {
			matcher = Predicates.alwaysTrue();
		}
		FluidStack target = FluidStack.EMPTY;
		for (Tag tag : accessor.getServerData().getList("jadeTanks", Tag.TAG_COMPOUND)) {
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
			IElementHelper elements = tooltip.getElementHelper();
			tooltip.add(elements.fluid(target));
			tooltip.append(elements.spacer(2, 0));
			tooltip.append(target.getDisplayName());
			IElement amount;
			if (target == fluid) {
				amount = elements.text(new TranslatableComponent("jade.fluid.empty"));
			} else {
				amount = elements.text(new TextComponent(TConstructPlugin.client.getDisplayHelper().humanReadableNumber(target.getAmount(), "B", true)));
			}
			amount.size(new Vec2(amount.getSize().x + 18, 2)).translate(new Vec2(18, -7));
			tooltip.add(amount);
		}
	}

}
*/