package snownee.jadeaddons.create;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import snownee.jade.addon.core.ObjectNameProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.JadeIds;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.ui.TextElement;
import snownee.jadeaddons.JadeAddonsBase;

public enum ContraptionExactBlockProvider implements IEntityComponentProvider {
	INSTANCE;

	private final Cache<Entity, Accessor<?>> accessorCache = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(
			100,
			TimeUnit.MILLISECONDS).build();

	@Override
	public @Nullable Element getIcon(EntityAccessor accessor, IPluginConfig config, @Nullable Element currentIcon) {
		Accessor<?> exact = accessorCache.getIfPresent(accessor.getEntity());
		if (exact == null) {
			return null;
		}
		return JadeAddonsBase.client().getAccessorHandler(exact.getAccessorType()).getIcon(exact);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		Accessor<?> exact = accessorCache.getIfPresent(accessor.getEntity());
		if (exact == null) {
			return;
		}
		ITooltip dummy = JadeUI.tooltip();
		if (exact instanceof BlockAccessor blockAccessor) {
			ObjectNameProvider.ForBlock.INSTANCE.appendTooltip(dummy, blockAccessor, config);
		} else if (exact instanceof EntityAccessor entityAccessor) {
			ObjectNameProvider.ForEntity.INSTANCE.appendTooltip(dummy, entityAccessor, config);
		}
		List<LayoutElement> elements = dummy.get(JadeIds.CORE_OBJECT_NAME);
		if (elements.isEmpty()) {
			return;
		}
		tooltip.remove(JadeIds.CORE_OBJECT_NAME);
		tooltip.add(0, elements.stream().map(e -> {
			if (e instanceof TextElement textElement) {
				Component narration = textElement.getNarration();
				if (narration != null) {
					return JadeUI.text(IThemeHelper.get().title(narration).copy().withStyle(ChatFormatting.ITALIC))
							.tag(JadeIds.CORE_OBJECT_NAME);
				}
			}
			return ((Element) e).tag(JadeIds.CORE_OBJECT_NAME);
		}).toList());
	}

	public void setHit(Entity entity, Accessor<?> accessor) {
		accessorCache.put(entity, accessor);
	}

	@Override
	public Identifier getUid() {
		return CreatePlugin.CONTRAPTION_EXACT_BLOCK;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.HEAD;
	}

}
