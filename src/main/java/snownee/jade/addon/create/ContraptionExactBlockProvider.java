package snownee.jade.addon.create;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import snownee.jade.addon.JadeAddonsBase;
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

	private final Cache<Entity, Accessor<?>> accessorCache = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(
			100,
			TimeUnit.MILLISECONDS).build();

	@Override
	public @Nullable IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
		Accessor<?> exact = accessorCache.getIfPresent(accessor.getEntity());
		if (exact == null) {
			return null;
		}
		return JadeAddonsBase.client.getAccessorHandler(exact.getAccessorType()).getIcon(exact);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		Accessor<?> exact = accessorCache.getIfPresent(accessor.getEntity());
		if (exact == null) {
			return;
		}
		ITooltip dummy = IElementHelper.get().tooltip();
		if (exact instanceof BlockAccessor blockAccessor) {
			ObjectNameProvider.INSTANCE.appendTooltip(dummy, blockAccessor, config);
		} else if (exact instanceof EntityAccessor entityAccessor) {
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

	public void setHit(Entity entity, Accessor<?> accessor) {
		accessorCache.put(entity, accessor);
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
