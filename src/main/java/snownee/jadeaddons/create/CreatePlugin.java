package snownee.jadeaddons.create;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

import com.zurrtum.create.client.content.trains.track.TrackBlockOutline;
import com.zurrtum.create.client.content.trains.track.TrackBlockOutline.BezierPointSelection;
import com.zurrtum.create.client.foundation.utility.RaycastHelper;
import com.zurrtum.create.client.foundation.utility.RaycastHelper.PredicateTraceResult;
import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;
import com.zurrtum.create.content.contraptions.Contraption;
import com.zurrtum.create.content.decoration.placard.PlacardBlock;
import com.zurrtum.create.content.equipment.armor.BacktankBlock;
import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;
import com.zurrtum.create.content.fluids.tank.FluidTankBlockEntity;
import com.zurrtum.create.content.logistics.box.PackageEntity;
import com.zurrtum.create.content.logistics.tableCloth.TableClothBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlock;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.overlay.RayTracing;
import snownee.jadeaddons.JadeAddonsBase;

public class CreatePlugin implements IWailaPlugin {
	public static final String ID = "jadeaddons.create";
	public static final Identifier PLACARD = Identifier.fromNamespaceAndPath(ID, "placard");
	public static final Identifier BLAZE_BURNER = Identifier.fromNamespaceAndPath(ID, "blaze_burner");
	public static final Identifier CONTRAPTION_INVENTORY = Identifier.fromNamespaceAndPath(ID, "contraption_inv");
	public static final Identifier CONTRAPTION_FLUIDS = Identifier.fromNamespaceAndPath(ID, "contraption_fluids");
	public static final Identifier CONTRAPTION_EXACT_BLOCK = Identifier.fromNamespaceAndPath(ID, "exact_block");
	public static final Identifier FILTER = Identifier.fromNamespaceAndPath(ID, "filter");
	public static final Identifier HIDE_BOILER_TANKS = Identifier.fromNamespaceAndPath(ID, "hide_boiler_tanks");
	public static final Identifier BACKTANK_CAPACITY = Identifier.fromNamespaceAndPath(ID, "backtank_capacity");
	public static final Identifier GOGGLES = Identifier.fromNamespaceAndPath(ID, "goggles");
	public static final Identifier REQUIRES_GOGGLES = Identifier.fromNamespaceAndPath(ID, "goggles.requires_goggles");
	public static final Identifier GOGGLES_DETAILED = Identifier.fromNamespaceAndPath(ID, "goggles.detailed");
	public static final Identifier PACKAGE = Identifier.fromNamespaceAndPath(ID, "package");
	public static final Identifier TABLE_CLOTH = Identifier.fromNamespaceAndPath(ID, "table_cloth");

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(BlazeBurnerProvider.INSTANCE, BlazeBurnerBlockEntity.class);
		registration.registerBlockDataProvider(BacktankProvider.INSTANCE, BacktankBlockEntity.class);
		registration.registerItemStorage(ContraptionItemStorageProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerItemStorage(PackageProvider.INSTANCE, PackageEntity.class);
		registration.registerItemStorage(TableClothProvider.INSTANCE, TableClothBlockEntity.class);
		registration.registerFluidStorage(ContraptionFluidStorageProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerFluidStorage(HideBoilerHandlerProvider.INSTANCE, FluidTankBlockEntity.class);

		BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse("create:super_glue"))
				.flatMap(BuiltInRegistries.ENTITY_TYPE::getResourceKey)
				.ifPresent(registration.entityTypeOperations()::hide);
	}

	// See ContraptionHandlerClient
	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(REQUIRES_GOGGLES, true);
		registration.addConfig(GOGGLES_DETAILED, false);
		registration.registerBlockComponent(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerBlockIcon(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerBlockComponent(BlazeBurnerProvider.INSTANCE, BlazeBurnerBlock.class);
		registration.registerEntityIcon(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerEntityComponent(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerBlockComponent(FilterProvider.INSTANCE, Block.class);
		registration.registerBlockComponent(BacktankProvider.INSTANCE, BacktankBlock.class);
		registration.registerBlockComponent(new GogglesProvider(), Block.class);

		registration.registerItemStorageClient(ContraptionItemStorageProvider.INSTANCE);
		registration.registerItemStorageClient(PackageProvider.INSTANCE);
		registration.registerItemStorageClient(TableClothProvider.INSTANCE);
		registration.registerFluidStorageClient(ContraptionFluidStorageProvider.INSTANCE);
		registration.registerFluidStorageClient(HideBoilerHandlerProvider.INSTANCE);

		registration.markAsClientFeature(REQUIRES_GOGGLES);
		registration.markAsClientFeature(GOGGLES_DETAILED);

		RayTracing.INSTANCE.entityFilter = RayTracing.INSTANCE.entityFilter.and(e -> {
			if (!(e instanceof AbstractContraptionEntity contraptionEntity)) {
				return true;
			}
			Minecraft mc = Minecraft.getInstance();
			if (!(mc.getCameraEntity() instanceof Player camera)) {
				return true;
			}
			float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
			Vec3 origin = camera.getEyePosition(partialTick);
			Vec3 lookVector = camera.getViewVector(partialTick);
			float reach = (float) camera.blockInteractionRange() + IWailaConfig.get().general().getExtendedReach();
			Vec3 target = origin.add(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach);
			Vec3 localOrigin = contraptionEntity.toLocalVector(origin, 1);
			Vec3 localTarget = contraptionEntity.toLocalVector(target, 1);
			Contraption contraption = Objects.requireNonNull(contraptionEntity.getContraption());
			Level level = Objects.requireNonNull(mc.level);
			PredicateTraceResult predicateResult = RaycastHelper.rayTraceUntil(
					localOrigin, localTarget, p -> {
						StructureBlockInfo blockInfo = contraption.getBlocks().get(p);
						if (blockInfo == null) {
							return false;
						}
						BlockState state = blockInfo.state();
						VoxelShape raytraceShape = state.getShape(level, BlockPos.ZERO);
						if (raytraceShape.isEmpty()) {
							return false;
						}
						BlockHitResult rayTrace = raytraceShape.clip(localOrigin, localTarget, p);
						if (IWailaConfig.get().plugin().get(CONTRAPTION_EXACT_BLOCK) && rayTrace != null &&
								rayTrace.getType() != Type.MISS) {
							BlockAccessor originalAccessor = JadeAddonsBase.client().blockAccessor().blockState(state).hit(rayTrace).build();
							Accessor<?> accessor = originalAccessor;
							for (JadeRayTraceCallback callback : WailaClientRegistration.instance().rayTraceCallback.callbacks()) {
								accessor = callback.onRayTrace(rayTrace, accessor, originalAccessor);
								if (accessor == null) {
									break;
								}
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
	public @Nullable Accessor<?> override(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) {
		BezierPointSelection result = TrackBlockOutline.result;
		if (result == null) {
			return accessor;
		}
		if (originalAccessor instanceof EntityAccessor) {
			return accessor;
		}
		BlockHitResult trackHit = new BlockHitResult(
				Vec3.atCenterOf(result.blockEntity().getBlockPos()),
				Direction.UP,
				result.blockEntity().getBlockPos(),
				false);
		return JadeAddonsBase.client().blockAccessor()
				.blockState(result.blockEntity().getBlockState())
				.blockEntity(result.blockEntity())
				.hit(trackHit)
				.build();
	}

}
