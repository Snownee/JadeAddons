package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerTileEntity;
import com.simibubi.create.content.curiosities.tools.BlueprintEntity;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.RaycastHelper.PredicateTraceResult;

import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.overlay.RayTracing;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@WailaPlugin(CreatePlugin.ID)
public class CreatePlugin implements IWailaPlugin {
	public static final String ID = "create";
	public static final ResourceLocation CRAFTING_BLUEPRINT = new ResourceLocation(ID, "crafting_blueprint");
	public static final ResourceLocation BLAZE_BURNER = new ResourceLocation(ID, "blaze_burner");
	public static final ResourceLocation CONTRAPTION = new ResourceLocation(ID, "contraption");
	static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(CRAFTING_BLUEPRINT, true);
		registration.addConfig(BLAZE_BURNER, true);
		registration.addConfig(CONTRAPTION, true);
		registration.registerBlockDataProvider(BlazeBurnerProvider.INSTANCE, BlazeBurnerTileEntity.class);
		registration.registerEntityDataProvider(ContraptionProvider.INSTANCE, AbstractContraptionEntity.class);
	}

	// See ContraptionHandlerClient
	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.registerComponentProvider(CraftingBlueprintProvider.INSTANCE, TooltipPosition.BODY, BlueprintEntity.class);
		registration.registerIconProvider(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
		registration.registerComponentProvider(BlazeBurnerProvider.INSTANCE, TooltipPosition.BODY, BlazeBurnerBlock.class);
		registration.registerComponentProvider(ContraptionProvider.INSTANCE, TooltipPosition.BODY, AbstractContraptionEntity.class);

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
				return rayTrace != null;
			});
			return predicateResult != null && !predicateResult.missed();
		});
	}

}
