package snownee.jade.addon.create;

import java.lang.reflect.Field;

import com.simibubi.create.content.curiosities.tools.BlueprintOverlayRenderer;

import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.addon.JadeAddons;

public enum CraftingBlueprintProvider implements IEntityComponentProvider {
	INSTANCE;

	public static Field RESULT;

	static {
		try {
			RESULT = BlueprintOverlayRenderer.class.getDeclaredField("result");
			RESULT.setAccessible(true);
		} catch (Throwable e) {
			JadeAddons.LOGGER.catching(e);
			RESULT = null;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		if (!config.get(CreatePlugin.CRAFTING_BLUEPRINT)) {
			return;
		}
		ItemStack result = getResult();
		if (!result.isEmpty()) {
			tooltip.add(result.getHoverName());
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (config.get(CreatePlugin.CRAFTING_BLUEPRINT)) {
			ItemStack result = getResult();
			if (!result.isEmpty()) {
				return CreatePlugin.client.getElementHelper().item(result);
			}
		}
		return currentIcon;
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemStack getResult() {
		if (RESULT != null) {
			try {
				return (ItemStack) RESULT.get(null);
			} catch (Throwable e) {
				JadeAddons.LOGGER.catching(e);
				e.printStackTrace();
			}
		}
		return ItemStack.EMPTY;
	}

}
