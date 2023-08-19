package snownee.jade.addon.create;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.core.ObjectNameProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElement.Align;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.TextElement;

public enum ContraptionExactBlockProvider implements IEntityComponentProvider {
	INSTANCE;

	private long time = Long.MIN_VALUE;
	private Accessor<?> accessor;

	@Override
	public @Nullable IElement getIcon(EntityAccessor ignored, IPluginConfig config, IElement currentIcon) {
		if (validate()) {
			return CreatePlugin.client.getAccessorHandler(accessor.getAccessorType()).getIcon(accessor);
		}
		return null;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor ignored, IPluginConfig config) {
		if (!validate()) {
			return;
		}
		ITooltip dummy = IElementHelper.get().tooltip();
		if (accessor instanceof BlockAccessor blockAccessor) {
			ObjectNameProvider.INSTANCE.appendTooltip(dummy, blockAccessor, config);
		} else if (accessor instanceof EntityAccessor entityAccessor) {
			ObjectNameProvider.INSTANCE.appendTooltip(dummy, entityAccessor, config);
		}
		if (!dummy.isEmpty()) {
			// this is shitty... improve it one day
			tooltip.remove(Identifiers.CORE_OBJECT_NAME);
			tooltip.add(0, dummy.get(0, Align.LEFT).stream().map(e -> {
				if (e instanceof TextElement text) {
					e = IElementHelper.get().text(IThemeHelper.get().title(text.text.getString()).copy().withStyle(ChatFormatting.ITALIC));
				}
				return e.tag(Identifiers.CORE_OBJECT_NAME);
			}).toList());
		}
	}

	public void setHit(Accessor<?> accessor) {
		this.accessor = accessor;
		time = Util.getMillis();
	}

	private boolean validate() {
		if (accessor == null || (Util.getMillis() - time) > 10) {
			return false;
		}
		if (accessor instanceof EntityAccessor entityAccessor && entityAccessor.getEntity() instanceof AbstractContraptionEntity) {
			return false;
		}
		return true;
	}

	@Override
	public ResourceLocation getUid() {
		return CreatePlugin.CONTRAPTION_EXACT_BLOCK;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.HEAD;
	}

}
