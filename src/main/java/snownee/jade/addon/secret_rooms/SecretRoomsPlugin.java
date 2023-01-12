package snownee.jade.addon.secret_rooms;

import org.jetbrains.annotations.Nullable;

import com.wynprice.secretrooms.server.blocks.SecretBaseBlock;
import com.wynprice.secretrooms.server.blocks.SecretBlocks;
import com.wynprice.secretrooms.server.items.TrueVisionGogglesClientHandler;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.impl.config.PluginConfig;

@WailaPlugin(SecretRoomsPlugin.ID)
public class SecretRoomsPlugin implements IWailaPlugin {
	public static final String ID = "secretroomsmod";
	public static final ResourceLocation ENABLED = new ResourceLocation(ID, JadeAddons.ID);
	private static IWailaClientRegistration client;

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.addConfig(ENABLED, true);
		registration.addRayTraceCallback(this::override);
	}

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public Accessor<?> override(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> original) {
		if (!PluginConfig.INSTANCE.get(ENABLED))
			return accessor;
		if (!(accessor instanceof BlockAccessor))
			return accessor;
		if (accessor.getPlayer().isSpectator())
			return accessor;
		BlockAccessor blockAccessor = (BlockAccessor) accessor;
		Block block = blockAccessor.getBlock();
		if (!ID.equals(Registry.BLOCK.getKey(block).getNamespace()))
			return accessor;
		if (TrueVisionGogglesClientHandler.isWearingGoggles(accessor.getPlayer()))
			return accessor;
		BlockState mirror;
		if (block == SecretBlocks.TORCH_LEVER.get()) {
			mirror = Blocks.TORCH.defaultBlockState();
		} else if (block == SecretBlocks.WALL_TORCH_LEVER.get()) {
			mirror = Blocks.WALL_TORCH.defaultBlockState();
		} else {
			mirror = SecretBaseBlock.getMirrorState(accessor.getLevel(), blockAccessor.getPosition()).orElse(null);
		}
		if (mirror == null)
			return accessor;
		/* off */
		return client.blockAccessor()
				.blockState(mirror)
				.blockEntity(null)
				.hit(blockAccessor.getHitResult())
				.build();
		/* on */
	}

}
