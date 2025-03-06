package snownee.jade.addon.tconstruct;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum CastingTableProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(TConstructPlugin.CASTING_TABLE)) {
			return;
		}
		if (accessor.getBlockEntity() instanceof CastingBlockEntity castingBlock && castingBlock.getCoolingTime() > 0) {
			IElementHelper elements = IElementHelper.get();
			IElement progress = elements.progress(
					(float) castingBlock.getTimer() / castingBlock.getCoolingTime(),
					null,
					elements.progressStyle(),
					BoxStyle.DEFAULT,
					true).tag(TConstructPlugin.CASTING_TABLE);
			tooltip.add(progress);
		}
	}

	@Override
	public ResourceLocation getUid() {
		return TConstructPlugin.CASTING_TABLE;
	}
}
