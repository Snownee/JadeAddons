/*package snownee.jade.addon.general;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;

@WailaPlugin(GeneralPlugin.ID)
public class GeneralPlugin implements IWailaPlugin {
	public static final String ID = "jadeaddons";
	public static final ResourceLocation EQUIPMENT_REQUIREMENT = new ResourceLocation(ID, "equipment_requirement");
	public static final ResourceLocation EQUIPMENT_DETAILS_REQUIREMENT = new ResourceLocation(ID, "equipment_details_requirement");
	static IWailaClientRegistration client;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClient(IWailaClientRegistration registration) {
		client = registration;
		registration.addConfig(EQUIPMENT_REQUIREMENT, "", ResourceLocation::isValidResourceLocation);
		registration.addConfig(EQUIPMENT_DETAILS_REQUIREMENT, "", ResourceLocation::isValidResourceLocation);
		registration.addRayTraceCallback(10000, this::override);
	}

	@OnlyIn(Dist.CLIENT)
	public Accessor<?> override(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) {
		IPluginConfig config = IWailaConfig.get().getPlugin();
		TagKey<Block> tag = null;
		String tagId = config.getString(EQUIPMENT_DETAILS_REQUIREMENT);
		TagKey.create(null, EQUIPMENT_DETAILS_REQUIREMENT);
		if (accessor instanceof BlockAccessor blockAccessor) {
			return client.blockAccessor().from(blockAccessor).showDetails(false).build();
		}
	}

}
*/