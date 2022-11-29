package snownee.jade.addon.create;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerTileEntity;
import com.simibubi.create.content.curiosities.deco.PlacardBlock;
import com.simibubi.create.content.curiosities.tools.BlueprintEntity;
import com.simibubi.create.content.logistics.trains.track.TrackBlockOutline;
import com.simibubi.create.content.logistics.trains.track.TrackBlockOutline.BezierPointSelection;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.RaycastHelper.PredicateTraceResult;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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
import snownee.jade.api.Accessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.impl.ObjectDataCenter;
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
	static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(BlazeBurnerProvider.INSTANCE, BlazeBurnerTileEntity.class);
		registration.registerItemStorage(ContraptionItemStorageProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerFluidStorage(ContraptionFluidStorageProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerFluidStorage(HideBoilerHandlerProvider.INSTANCE, FluidTankTileEntity.class);
	}

	// See ContraptionHandlerClient
	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.registerEntityComponent(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
		registration.registerEntityIcon(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
		registration.registerBlockComponent(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerBlockIcon(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerBlockComponent(BlazeBurnerProvider.INSTANCE, BlazeBurnerBlock.class);
		registration.registerEntityIcon(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerEntityComponent(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerBlockComponent(FilterProvider.INSTANCE, Block.class);

		registration.registerItemStorageClient(ContraptionItemStorageProvider.INSTANCE);
		registration.registerFluidStorageClient(ContraptionFluidStorageProvider.INSTANCE);
		registration.registerFluidStorageClient(HideBoilerHandlerProvider.INSTANCE);

		RayTracing.ENTITY_FILTER = RayTracing.ENTITY_FILTER.and(e -> {
			if (!(e instanceof AbstractContraptionEntity)) {
				return true;
			}
			Minecraft mc = Minecraft.getInstance();
			Entity camera = mc.getCameraEntity();
			Vec3 origin = camera.getEyePosition(mc.getFrameTime());
			Vec3 lookVector = camera.getViewVector(mc.getFrameTime());
			float reach = mc.gameMode.getPickRange() + client.getConfig().getGeneral().getReachDistance();
			Vec3 target = origin.add(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach);
			AbstractContraptionEntity contraptionEntity = (AbstractContraptionEntity) e;
			Vec3 localOrigin = contraptionEntity.toLocalVector(origin, 1);
			Vec3 localTarget = contraptionEntity.toLocalVector(target, 1);
			Contraption contraption = contraptionEntity.getContraption();
			PredicateTraceResult predicateResult = RaycastHelper.rayTraceUntil(localOrigin, localTarget, p -> {
				StructureBlockInfo blockInfo = contraption.getBlocks().get(p);
				if (blockInfo == null)
					return false;
				BlockState state = blockInfo.state;
				VoxelShape raytraceShape = state.getShape(Minecraft.getInstance().level, BlockPos.ZERO);
				if (raytraceShape.isEmpty())
					return false;
				BlockHitResult rayTrace = raytraceShape.clip(localOrigin, localTarget, p);
				if (rayTrace != null && rayTrace.getType() != Type.MISS) {
					ContraptionExactBlockProvider.INSTANCE.setHit(rayTrace, state);
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
		BlockHitResult trackHit = new BlockHitResult(Vec3.atCenterOf(result.te().getBlockPos()), Direction.UP, result.te().getBlockPos(), false);
		/* off */
		return client.blockAccessor()
				.blockState(result.te().getBlockState())
				.blockEntity(result.te())
				.level(Minecraft.getInstance().level)
				.player(Minecraft.getInstance().player)
				.hit(trackHit)
				.serverConnected(ObjectDataCenter.serverConnected)
				.serverData(ObjectDataCenter.getServerData())
				.build();
		/* on */
	}

}
