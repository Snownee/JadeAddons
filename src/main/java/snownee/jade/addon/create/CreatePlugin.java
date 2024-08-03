package snownee.jade.addon.create;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.equipment.armor.BacktankBlock;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.blueprint.BlueprintEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.trains.track.TrackBlockOutline;
import com.simibubi.create.content.trains.track.TrackBlockOutline.BezierPointSelection;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.RaycastHelper.PredicateTraceResult;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.addon.JadeAddonsBase;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.overlay.RayTracing;

@WailaPlugin(CreatePlugin.ID)
public class CreatePlugin implements IWailaPlugin {
	public static final String ID = "create";
	public static final ResourceLocation CRAFTING_BLUEPRINT = new ResourceLocation(ID, "crafting_blueprint");
	public static final ResourceLocation PLACARD = new ResourceLocation(ID, "placard");
	public static final ResourceLocation BLAZE_BURNER = new ResourceLocation(ID, "blaze_burner");
	public static final ResourceLocation CONTRAPTION_INVENTORY = new ResourceLocation(ID, "contraption_inv");
	public static final ResourceLocation CONTRAPTION_EXACT_BLOCK = new ResourceLocation(ID, "exact_block");
	public static final ResourceLocation FILTER = new ResourceLocation(ID, "filter");
	public static final ResourceLocation HIDE_BOILER_TANKS = new ResourceLocation(ID, "hide_boiler_tanks");
	public static final ResourceLocation BACKTANK_CAPACITY = new ResourceLocation(ID, "backtank_capacity");
	public static final ResourceLocation GOGGLES = new ResourceLocation(ID, "goggles");
	public static final ResourceLocation REQUIRES_GOGGLES = new ResourceLocation(ID, "goggles.requires_goggles");
	public static final ResourceLocation GOGGLES_DETAILED = new ResourceLocation(ID, "goggles.detailed");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(BlazeBurnerProvider.INSTANCE, BlazeBurnerBlockEntity.class);
		registration.registerBlockDataProvider(BacktankProvider.INSTANCE, BacktankBlockEntity.class);
		registration.registerItemStorage(ContraptionItemStorageProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerFluidStorage(ContraptionFluidStorageProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerFluidStorage(HideBoilerHandlerProvider.INSTANCE, FluidTankBlockEntity.class);
	}

	// See ContraptionHandlerClient
	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(REQUIRES_GOGGLES, true);
		registration.addConfig(GOGGLES_DETAILED, false);
		registration.registerEntityComponent(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
		registration.registerEntityIcon(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
		registration.registerBlockComponent(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerBlockIcon(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerBlockComponent(BlazeBurnerProvider.INSTANCE, BlazeBurnerBlock.class);
		registration.registerEntityIcon(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerEntityComponent(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerBlockComponent(FilterProvider.INSTANCE, Block.class);
		registration.registerBlockComponent(BacktankProvider.INSTANCE, BacktankBlock.class);
		registration.registerBlockComponent(new GogglesProvider(), Block.class);

		registration.registerItemStorageClient(ContraptionItemStorageProvider.INSTANCE);
		registration.registerFluidStorageClient(ContraptionFluidStorageProvider.INSTANCE);
		registration.registerFluidStorageClient(HideBoilerHandlerProvider.INSTANCE);

		EntityType.byString("create:super_glue").ifPresent(registration::hideTarget);

		RayTracing.ENTITY_FILTER = RayTracing.ENTITY_FILTER.and(e -> {
			if (!(e instanceof AbstractContraptionEntity contraptionEntity)) {
				return true;
			}
			Minecraft mc = Minecraft.getInstance();
			Entity camera = mc.getCameraEntity();
			if (camera == null) {
				return true;
			}
			Vec3 origin = camera.getEyePosition(mc.getFrameTime());
			Vec3 lookVector = camera.getViewVector(mc.getFrameTime());
			float reach = mc.gameMode.getPickRange() + IWailaConfig.get().getGeneral().getReachDistance();
			Vec3 target = origin.add(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach);
			Vec3 localOrigin = contraptionEntity.toLocalVector(origin, 1);
			Vec3 localTarget = contraptionEntity.toLocalVector(target, 1);
			Contraption contraption = contraptionEntity.getContraption();
			PredicateTraceResult predicateResult = RaycastHelper.rayTraceUntil(localOrigin, localTarget, p -> {
				StructureBlockInfo blockInfo = contraption.getBlocks().get(p);
				if (blockInfo == null) {
					return false;
				}
				BlockState state = blockInfo.state();
				VoxelShape raytraceShape = state.getShape(Minecraft.getInstance().level, BlockPos.ZERO);
				if (raytraceShape.isEmpty()) {
					return false;
				}
				BlockHitResult rayTrace = raytraceShape.clip(localOrigin, localTarget, p);
				if (IWailaConfig.get().getPlugin().get(CONTRAPTION_EXACT_BLOCK) && rayTrace != null && rayTrace.getType() != Type.MISS) {
					BlockAccessor originalAccessor = JadeAddonsBase.client.blockAccessor().blockState(state).hit(rayTrace).build();
					Accessor<?> accessor = originalAccessor;
					for (JadeRayTraceCallback callback : WailaClientRegistration.INSTANCE.rayTraceCallback.callbacks()) {
						accessor = callback.onRayTrace(rayTrace, accessor, originalAccessor);
					}
					if (accessor != null) {
						ContraptionExactBlockProvider.INSTANCE.setHit(contraptionEntity, accessor);
					}
				}
				return rayTrace != null;
			});
			return predicateResult != null && !predicateResult.missed();
		});

		registration.addRayTraceCallback(this::override);
	}

	@OnlyIn(Dist.CLIENT)
	public Accessor<?> override(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) {
		BezierPointSelection result = TrackBlockOutline.result;
		if (result == null) {
			return accessor;
		}
		if (originalAccessor instanceof EntityAccessor) {
			return accessor;
		}
		BlockHitResult trackHit = new BlockHitResult(Vec3.atCenterOf(result.blockEntity().getBlockPos()),
				Direction.UP,
				result.blockEntity().getBlockPos(),
				false);
		/* off */
		return JadeAddonsBase.client.blockAccessor()
				.blockState(result.blockEntity().getBlockState())
				.blockEntity(result.blockEntity())
				.hit(trackHit)
				.build();
		/* on */
	}

}
