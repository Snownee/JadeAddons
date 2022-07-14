package snownee.jade.addon.deep_resonance;

import mcjty.deepresonance.modules.generator.block.GeneratorPartTileEntity;
import mcjty.deepresonance.modules.generator.data.GeneratorBlob;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum GeneratorPartProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(DeepResonancePlugin.GENERATOR_PART) || !accessor.getServerData().contains("DeepResonance")) {
			return;
		}
		CompoundTag tag = accessor.getServerData().getCompound("DeepResonance");
		int id = tag.getInt("Id");
		int collectors = tag.getInt("Collectors");
		int generators = tag.getInt("Generators");
		tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.id", id));
		tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.collectors", collectors));
		tooltip.add(new TranslatableComponent("jadeaddons.deepresonance.generators", generators));
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

}
