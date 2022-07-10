package snownee.jade.addon.tconstruct;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.event.WailaRayTraceEvent;
import mcp.mobius.waila.impl.BlockAccessorImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.smeltery.block.AbstractCastingBlock;
import slimeknights.tconstruct.smeltery.block.FaucetBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDrainBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock;

@WailaPlugin(TConstructPlugin.ID)
public class TConstructPlugin implements IWailaPlugin {
	public static final String ID = "tconstruct";
	public static final ResourceLocation CASTING_TABLE = new ResourceLocation(ID, "casting_table");
	public static final ResourceLocation DRAIN = new ResourceLocation(ID, "drain");
	static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(CASTING_TABLE, true);
		registration.addConfig(DRAIN, true);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.registerComponentProvider(CastingTableProvider.INSTANCE, TooltipPosition.BODY, AbstractCastingBlock.class);
		registration.registerComponentProvider(DrainProvider.INSTANCE, TooltipPosition.BODY, SearedDuctBlock.class);
		registration.registerComponentProvider(DrainProvider.INSTANCE, TooltipPosition.BODY, SearedDrainBlock.class);
		MinecraftForge.EVENT_BUS.addListener(this::override);
	}

	public void override(WailaRayTraceEvent event) {
		if (event.getAccessor() instanceof BlockAccessor accessor) {
			BlockState state = accessor.getBlockState();
			if (!(state.getBlock() instanceof FaucetBlock)) {
				return;
			}
			BlockPos posRel = accessor.getPosition().relative(state.getValue(FaucetBlock.FACING).getOpposite());
			BlockState stateRel = accessor.getLevel().getBlockState(posRel);
			if (stateRel.getBlock() instanceof SearedDuctBlock || stateRel.getBlock() instanceof SearedDrainBlock) {
				BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf(posRel), accessor.getHitResult().getDirection(), posRel, true);
				BlockAccessorImpl newAccessor = new BlockAccessorImpl(stateRel, accessor.getLevel().getBlockEntity(posRel), accessor.getLevel(), accessor.getPlayer(), accessor.getServerData(), hitResult, accessor.isServerConnected(), accessor.getPickedResult());
				event.setAccessor(newAccessor);
			}
		}
	}

}
