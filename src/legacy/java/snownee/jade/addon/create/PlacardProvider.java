package snownee.jade.addon.create;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

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
	public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (accessor.getBlockEntity() instanceof PlacardBlockEntity placard) {
			if (!placard.getHeldItem().isEmpty()) {
				return IElementHelper.get().item(placard.getHeldItem());
			}
		}
		return null;
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.PLACARD;
	}

}
