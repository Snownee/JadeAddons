package snownee.jade.addon.tconstruct;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import slimeknights.tconstruct.smeltery.block.AbstractCastingBlock;
import slimeknights.tconstruct.smeltery.block.FaucetBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDrainBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class TConstructPlugin implements IWailaPlugin {
	public static final String ID = "jadeaddons.tconstruct";
	public static final ResourceLocation CASTING_TABLE = new ResourceLocation(ID, "casting_table");
	public static final ResourceLocation DRAIN = new ResourceLocation(ID, "drain");

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(CastingTableProvider.INSTANCE, AbstractCastingBlock.class);
		registration.registerBlockComponent(DrainProvider.INSTANCE, SearedDuctBlock.class);
		registration.registerBlockComponent(DrainProvider.INSTANCE, SearedDrainBlock.class);
		registration.addRayTraceCallback((HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> original) -> {
					if (accessor instanceof BlockAccessor blockAccessor) {
						BlockState state = blockAccessor.getBlockState();
						if (!(state.getBlock() instanceof FaucetBlock)) {
							return accessor;
						}
						BlockPos posRel = blockAccessor.getPosition().relative(state.getValue(FaucetBlock.FACING).getOpposite());
						BlockState stateRel = accessor.getLevel().getBlockState(posRel);
						if (stateRel.getBlock() instanceof SearedDuctBlock || stateRel.getBlock() instanceof SearedDrainBlock) {
//							BlockHitResult hitResult = new BlockHitResult(
//									Vec3.atCenterOf(posRel),
//									blockAccessor.getHitResult().getDirection(),
//									posRel,
//									true);
							return registration.blockAccessor().from(blockAccessor).blockEntity(accessor.getLevel().getBlockEntity(posRel)).build();
						}
					}
					return accessor;
				}
		);
	}

}