package snownee.jadeaddons.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.client.api.goggles.IHaveGoggleInformation;
import com.zurrtum.create.client.api.goggles.IHaveHoveringInformation;
import com.zurrtum.create.client.content.contraptions.IDisplayAssemblyExceptions;
import com.zurrtum.create.client.content.equipment.goggles.GoggleOverlayRenderer;
import com.zurrtum.create.client.foundation.utility.CreateLang;
import com.zurrtum.create.content.contraptions.piston.MechanicalPistonBlock;
import com.zurrtum.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.zurrtum.create.content.equipment.goggles.GogglesItem;
import com.zurrtum.create.content.fluids.drain.ItemDrainBlockEntity;
import com.zurrtum.create.content.fluids.spout.SpoutBlockEntity;
import com.zurrtum.create.content.fluids.tank.FluidTankBlockEntity;
import com.zurrtum.create.content.processing.basin.BasinBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.common.NeoForge;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.JadeUI;

// See GoggleOverlayRenderer
public class GogglesProvider implements IBlockComponentProvider {

	private static final Identifier CREATE_OVERLAY_ID = Identifier.fromNamespaceAndPath("create", "goggle_info");
	private static final Set<String> REMOVE_KEYS = Set.of("create.tooltip.chute.contains", "create.tooltip.deployer.contains");

	private final Block PISTON_EXTENSION_POLE = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("create", "piston_extension_pole"));

	public GogglesProvider() {
		NeoForge.EVENT_BUS.addListener(this::hideCreateOverlay);
	}

	private void hideCreateOverlay(RenderGuiLayerEvent.Pre event) {
		if (event.getName().equals(CREATE_OVERLAY_ID) && IWailaConfig.get().plugin().get(CreatePlugin.GOGGLES)) {
			event.setCanceled(true);
		}
	}

	@Override
	public Identifier getUid() {
		return CreatePlugin.GOGGLES;
	}

	@Override
	public void appendTooltip(ITooltip tooltip1, BlockAccessor accessor, IPluginConfig config) {
		if (config.get(CreatePlugin.GOGGLES_DETAILED) && !accessor.showDetails()) {
			return;
		}
		Level world = accessor.getLevel();
		BlockPos pos = GoggleOverlayRenderer.proxiedOverlayPosition(world, accessor.getPosition());
		BlockEntity te = world.getBlockEntity(pos);

		boolean wearingGoggles = !config.get(CreatePlugin.REQUIRES_GOGGLES) || GogglesItem.isWearingGoggles(accessor.getPlayer());

		boolean hasGoggleInformation = te instanceof IHaveGoggleInformation
				&& !(te instanceof SpoutBlockEntity)
				&& !(te instanceof ItemDrainBlockEntity)
				&& !(te instanceof BasinBlockEntity)
				&& (
				!(te instanceof FluidTankBlockEntity tank) || (tank.getControllerBE() == null) || tank.getControllerBE().boiler.isActive());
		boolean hasHoveringInformation = te instanceof IHaveHoveringInformation;

		boolean goggleAddedInformation = false;
		boolean hoverAddedInformation = false;

		List<Component> tooltip = new ArrayList<>();

		if (hasGoggleInformation && wearingGoggles) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			goggleAddedInformation = gte.addToGoggleTooltip(tooltip, accessor.showDetails());
		}

		if (hasHoveringInformation) {
			if (!tooltip.isEmpty()) {
				tooltip.add(CommonComponents.EMPTY);
			}
			IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
			hoverAddedInformation = hte.addToTooltip(tooltip, accessor.showDetails());

			if (goggleAddedInformation && !hoverAddedInformation) {
				tooltip.removeLast();
			}
		}

		if (te instanceof IDisplayAssemblyExceptions exceptions) {
			boolean exceptionAdded = exceptions.addExceptionToTooltip(tooltip);
			if (exceptionAdded) {
				hasHoveringInformation = true;
				hoverAddedInformation = true;
			}
		}

		// break early if goggle or hover returned false when present
		if ((hasGoggleInformation && !goggleAddedInformation) && (hasHoveringInformation && !hoverAddedInformation)) {
			return;
		}

		tooltip.removeIf(c -> {
			for (Component sibling : c.getSiblings()) {
				if (sibling.getContents() instanceof TranslatableContents contents && REMOVE_KEYS.contains(contents.getKey())) {
					return true;
				}
			}
			return false;
		});
		tooltip.replaceAll(c -> {
			if (c.getContents() instanceof PlainTextContents literal && literal.text().startsWith("    ")) {
				MutableComponent mutableComponent = Component.literal(literal.text().substring(4)).withStyle(c.getStyle());
				c.getSiblings().forEach(mutableComponent::append);
				return mutableComponent;
			}
			return c;
		});

		// check for piston poles if goggles are worn
		BlockState state = world.getBlockState(pos);
		if (wearingGoggles && state.is(PISTON_EXTENSION_POLE)) {
			Direction[] directions = Iterate.directionsInAxis(state.getValue(PistonExtensionPoleBlock.FACING).getAxis());
			int poles = 1;
			boolean pistonFound = false;
			for (Direction dir : directions) {
				int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get().attachedPoles(world, pos, dir);
				poles += attachedPoles;
				pistonFound |= world.getBlockState(pos.relative(dir, attachedPoles + 1)).getBlock() instanceof MechanicalPistonBlock;
			}

			if (!pistonFound) {
				return;
			}
			if (!tooltip.isEmpty()) {
				tooltip.add(CommonComponents.EMPTY);
			}

			tooltip.add(CreateLang.translate("gui.goggles.pole_length").text(" " + poles).component());
		}

		tooltip.stream().map(c -> c.getString().isBlank() ? JadeUI.spacer(3, 3) : JadeUI.text(c)).forEach(tooltip1::add);
	}

	@Override
	public boolean enabledByDefault() {
		return false;
	}

}
