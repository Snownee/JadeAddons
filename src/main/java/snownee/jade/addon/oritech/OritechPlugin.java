package snownee.jade.addon.oritech;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import rearth.oritech.block.blocks.processing.MachineCoreBlock;
import rearth.oritech.util.Geometry;
import rearth.oritech.util.MultiblockMachineController;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class OritechPlugin implements IWailaPlugin {
	private static final int CORE_SEARCH_RADIUS = 4;

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> redirect(registration, accessor));
	}

	private static @Nullable Accessor<?> redirect(IWailaClientRegistration registration, Accessor<?> accessor) {
		if (!(accessor instanceof BlockAccessor blockAccessor)) {
			return accessor;
		}
		BlockState coreState = blockAccessor.getBlockState();
		if (!(coreState.getBlock() instanceof MachineCoreBlock) || !coreState.getValue(MachineCoreBlock.USED)) {
			return accessor;
		}
		Level level = blockAccessor.getLevel();
		BlockPos controllerPos = findController(level, blockAccessor.getPosition());
		if (controllerPos == null) {
			return accessor;
		}
		BlockEntity blockEntity = level.getBlockEntity(controllerPos);
		if (blockEntity == null) {
			return accessor;
		}
		BlockState blockState = level.getBlockState(controllerPos);
		BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(controllerPos), blockAccessor.getSide(), controllerPos, false);
		return registration.blockAccessor()
				.from(blockAccessor)
				.hit(hit)
				.blockState(blockState)
				.blockEntity(blockEntity)
				.build();
	}

	private static @Nullable BlockPos findController(Level level, BlockPos corePos) {
		BlockPos min = corePos.offset(-CORE_SEARCH_RADIUS, -CORE_SEARCH_RADIUS, -CORE_SEARCH_RADIUS);
		BlockPos max = corePos.offset(CORE_SEARCH_RADIUS, CORE_SEARCH_RADIUS, CORE_SEARCH_RADIUS);
		for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
			if (pos.equals(corePos)) {
				continue;
			}
			if (!(level.getBlockEntity(pos) instanceof MultiblockMachineController controller)) {
				continue;
			}
			Direction facing = controller.getFacingForMultiblock();
			BlockPos controllerPos = controller.getPosForMultiblock();
			for (Vec3i offset : controller.getCorePositions()) {
				if (controllerPos.offset(Geometry.rotatePosition(offset, facing)).equals(corePos)) {
					return controllerPos;
				}
			}
		}
		return null;
	}
}
