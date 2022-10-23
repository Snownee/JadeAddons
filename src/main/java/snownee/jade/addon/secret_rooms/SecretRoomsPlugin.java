/*package snownee.jade.addon.secret_rooms;

import com.wynprice.secretrooms.server.blocks.SecretBaseBlock;
import com.wynprice.secretrooms.server.blocks.SecretBlocks;
import com.wynprice.secretrooms.server.items.TrueVisionGogglesClientHandler;

import mcp.mobius.waila.api.Accessor;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.event.WailaRayTraceEvent;
import mcp.mobius.waila.impl.config.PluginConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import snownee.jade.addon.JadeAddons;

@WailaPlugin(SecretRoomsPlugin.ID)
public class SecretRoomsPlugin implements IWailaPlugin {
	public static final String ID = "secretroomsmod";
	public static final ResourceLocation MAIN = new ResourceLocation(ID, JadeAddons.ID);
	private static IWailaClientRegistration client;

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.addConfig(MAIN, true);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		MinecraftForge.EVENT_BUS.addListener(this::override);
	}

	@OnlyIn(Dist.CLIENT)
	public void override(WailaRayTraceEvent event) {
		if (!PluginConfig.INSTANCE.get(MAIN))
			return;
		Accessor<?> accessor = event.getAccessor();
		if (accessor.getPlayer().isSpectator())
			return;
		if (!(accessor instanceof BlockAccessor))
			return;
		BlockAccessor blockAccessor = (BlockAccessor) accessor;
		Block block = blockAccessor.getBlock();
		if (!ID.equals(block.getRegistryName().getNamespace()))
			return;
		if (TrueVisionGogglesClientHandler.isWearingGoggles(accessor.getPlayer()))
			return;
		BlockState mirror;
		if (block == SecretBlocks.TORCH_LEVER.get()) {
			mirror = Blocks.TORCH.defaultBlockState();
		} else if (block == SecretBlocks.WALL_TORCH_LEVER.get()) {
			mirror = Blocks.WALL_TORCH.defaultBlockState();
		} else {
			mirror = SecretBaseBlock.getMirrorState(accessor.getLevel(), blockAccessor.getPosition()).orElse(null);
		}
		if (mirror == null)
			return;
		event.setAccessor(client.createBlockAccessor(mirror, null, accessor.getLevel(), accessor.getPlayer(), null, blockAccessor.getHitResult(), accessor.isServerConnected()));
	}

}
*/