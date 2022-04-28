/*package snownee.jade.addon.tconstruct;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import mcp.mobius.waila.api.ui.IElementHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;

public enum CastingTableProvider implements IComponentProvider {
	INSTANCE;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(TConstructPlugin.CASTING_TABLE)) {
			return;
		}
		if (accessor.getBlockEntity() instanceof CastingBlockEntity castingBlock && castingBlock.getCoolingTime() > 0) {
			IElementHelper elements = tooltip.getElementHelper();
			IElement progress = elements.progress((float) castingBlock.getTimer() / castingBlock.getCoolingTime(), null, elements.progressStyle(), elements.borderStyle()).tag(TConstructPlugin.CASTING_TABLE);
			tooltip.add(progress);
		}
	}

}
*/