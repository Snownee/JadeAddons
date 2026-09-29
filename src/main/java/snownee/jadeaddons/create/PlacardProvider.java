package snownee.jadeaddons.create;

import org.jspecify.annotations.Nullable;

import com.zurrtum.create.content.decoration.placard.PlacardBlockEntity;

import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.JadeUI;

public enum PlacardProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (accessor.getBlockEntity() instanceof PlacardBlockEntity placard) {
			if (!placard.getHeldItem().isEmpty()) {
				tooltip.add(IDisplayHelper.get().stripColor(placard.getHeldItem().getHoverName()));
			}
		}
	}

	@Override
	public @Nullable Element getIcon(BlockAccessor accessor, IPluginConfig config, @Nullable Element currentIcon) {
		if (accessor.getBlockEntity() instanceof PlacardBlockEntity placard) {
			if (!placard.getHeldItem().isEmpty()) {
				return JadeUI.item(placard.getHeldItem());
			}
		}
		return null;
	}

	@Override
	public Identifier getUid() {
		return CreatePlugin.PLACARD;
	}

}
