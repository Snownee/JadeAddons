package snownee.jade.addon.tconstruct;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.smeltery.block.AbstractCastingBlock;
import slimeknights.tconstruct.smeltery.block.FaucetBlock;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.DrainBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

public class TConstructPlugin implements IWailaPlugin {
	public static final String ID = "jadeaddons.tconstruct";
	public static final ResourceLocation CASTING_TABLE = new ResourceLocation(ID, "casting_table");
	public static final ResourceLocation DUCT_FLUID = new ResourceLocation(ID, "duct_fluid");
	public static final ResourceLocation DUCT_ITEM = new ResourceLocation(ID, "duct_item");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerProgress(CastingTableProvider.INSTANCE, CastingBlockEntity.class);
		registration.registerFluidStorage(DuctFluidProvider.INSTANCE, DuctBlockEntity.class);
		registration.registerItemStorage(DuctItemProvider.INSTANCE, DuctBlockEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerProgressClient(CastingTableProvider.INSTANCE);
		registration.registerFluidStorageClient(DuctFluidProvider.INSTANCE);
		registration.registerItemStorageClient(DuctItemProvider.INSTANCE);
		registration.addRayTraceCallback((HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> original) -> {
					if (accessor instanceof BlockAccessor blockAccessor) {
						BlockState state = blockAccessor.getBlockState();
						if (!(state.getBlock() instanceof FaucetBlock)) {
							return accessor;
						}
						BlockPos posRel = blockAccessor.getPosition().relative(state.getValue(FaucetBlock.FACING).getOpposite());
						BlockEntity beRel = accessor.getLevel().getBlockEntity(posRel);
						if (beRel instanceof DuctBlockEntity || beRel instanceof DrainBlockEntity) {
							BlockState blockState = beRel.getBlockState();
							BlockHitResult blockHitResult = new BlockHitResult(
									Vec3.atCenterOf(posRel),
									blockAccessor.getHitResult().getDirection(),
									posRel,
									true);
							return registration.blockAccessor()
									.from(blockAccessor)
									.blockState(blockState)
									.blockEntity(beRel)
									.hit(blockHitResult)
									.build();
						}
					}
					return accessor;
				}
		);
	}

}