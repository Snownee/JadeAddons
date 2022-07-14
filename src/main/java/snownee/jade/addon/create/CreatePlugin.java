package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerTileEntity;
import com.simibubi.create.content.curiosities.deco.PlacardBlock;
import com.simibubi.create.content.curiosities.tools.BlueprintEntity;
import com.simibubi.create.content.logistics.trains.track.TrackBlockOutline;
import com.simibubi.create.content.logistics.trains.track.TrackBlockOutline.BezierPointSelection;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.RaycastHelper.PredicateTraceResult;

import mcp.mobius.waila.api.Accessor;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.event.WailaRayTraceEvent;
import mcp.mobius.waila.overlay.RayTracing;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@WailaPlugin(CreatePlugin.ID)
public class CreatePlugin implements IWailaPlugin {
	public static final String ID = "create";
	public static final ResourceLocation CRAFTING_BLUEPRINT = new ResourceLocation(ID, "crafting_blueprint");
	public static final ResourceLocation PLACARD = new ResourceLocation(ID, "placard");
	public static final ResourceLocation BLAZE_BURNER = new ResourceLocation(ID, "blaze_burner");
	public static final ResourceLocation CONTRAPTION_INVENTORY = new ResourceLocation(ID, "contraption_inv");
	public static final ResourceLocation CONTRAPTION_EXACT_BLOCK = new ResourceLocation(ID, "exact_block");
	static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(CRAFTING_BLUEPRINT, true);
		registration.addConfig(PLACARD, true);
		registration.addConfig(BLAZE_BURNER, true);
		registration.addConfig(CONTRAPTION_INVENTORY, true);
		registration.addConfig(CONTRAPTION_EXACT_BLOCK, true);
		registration.registerBlockDataProvider(BlazeBurnerProvider.INSTANCE, BlazeBurnerTileEntity.class);
		registration.registerEntityDataProvider(ContraptionInventoryProvider.INSTANCE, AbstractContraptionEntity.class);
	}

	// See ContraptionHandlerClient
	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.registerComponentProvider(CraftingBlueprintProvider.INSTANCE, TooltipPosition.BODY, BlueprintEntity.class);
		registration.registerIconProvider(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
		registration.registerComponentProvider(PlacardProvider.INSTANCE, TooltipPosition.BODY, PlacardBlock.class);
		registration.registerIconProvider(PlacardProvider.INSTANCE, PlacardBlock.class);
		registration.registerComponentProvider(BlazeBurnerProvider.INSTANCE, TooltipPosition.BODY, BlazeBurnerBlock.class);
		registration.registerComponentProvider(ContraptionInventoryProvider.INSTANCE, TooltipPosition.BODY, AbstractContraptionEntity.class);
		registration.registerIconProvider(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
		registration.registerComponentProvider(ContraptionExactBlockProvider.INSTANCE, TooltipPosition.HEAD, AbstractContraptionEntity.class);

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

		MinecraftForge.EVENT_BUS.addListener(this::override);
	}

	public void override(WailaRayTraceEvent event) {
		BezierPointSelection result = TrackBlockOutline.result;
		if (result == null) {
			return;
		}
		Accessor<?> accessor = event.getAccessor();
		if (accessor instanceof EntityAccessor) {
			return;
		}
		BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf(result.te().getBlockPos()), Direction.UP, result.te().getBlockPos(), false);
		BlockAccessor newAccessor = client.createBlockAccessor(result.te().getBlockState(), result.te(), accessor.getLevel(), accessor.getPlayer(), accessor.getServerData(), hitResult, accessor.isServerConnected());
		event.setAccessor(newAccessor);
	}

}
