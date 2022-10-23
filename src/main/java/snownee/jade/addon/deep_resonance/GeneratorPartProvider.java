package snownee.jade.addon.deep_resonance;

import mcjty.deepresonance.modules.generator.block.GeneratorPartTileEntity;
import mcjty.deepresonance.modules.generator.data.GeneratorBlob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GeneratorPartProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!accessor.getServerData().contains("DeepResonance")) {
			return;
		}
		CompoundTag tag = accessor.getServerData().getCompound("DeepResonance");
		int id = tag.getInt("Id");
		int collectors = tag.getInt("Collectors");
		int generators = tag.getInt("Generators");
		tooltip.add(Component.translatable("jadeaddons.deepresonance.id", id));
		tooltip.add(Component.translatable("jadeaddons.deepresonance.collectors", collectors));
		tooltip.add(Component.translatable("jadeaddons.deepresonance.generators", generators));
	}

	@Override
	public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean showDetails) {
		if (blockEntity instanceof GeneratorPartTileEntity part) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("Id", part.getMultiblockId());
			GeneratorBlob network = part.getBlob();
			tag.putInt("Collectors", network.getCollectorBlocks());
			tag.putInt("Generators", network.getGeneratorBlocks());
			data.put("DeepResonance", tag);
		}
	}

	@Override
	public ResourceLocation getUid() {
		return DeepResonancePlugin.GENERATOR_PART;
	}

}
