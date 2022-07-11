package snownee.jade.addon.create;

import org.jetbrains.annotations.Nullable;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import mcp.mobius.waila.impl.WailaClientRegistration;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public enum ContraptionExactBlockProvider implements IEntityComponentProvider {
	INSTANCE;

	static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation(Waila.MODID, "object_name");

	private long time = Long.MIN_VALUE;
	private BlockState hitBlock;
	private BlockHitResult hitResult;

	@Override
	public @Nullable IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (config.get(CreatePlugin.CONTRAPTION_EXACT_BLOCK) && validate()) {
			ItemStack stack = hitBlock.getCloneItemStack(hitResult, accessor.getLevel(), hitResult.getBlockPos(), accessor.getPlayer());
			return CreatePlugin.client.getElementHelper().item(stack);
		}
		return null;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		if (!config.get(CreatePlugin.CONTRAPTION_EXACT_BLOCK) || !validate()) {
			return;
		}
		Component name = null;
		if (WailaClientRegistration.INSTANCE.shouldPick(hitBlock)) {
			ItemStack pick = hitBlock.getCloneItemStack(hitResult, accessor.getLevel(), hitResult.getBlockPos(), accessor.getPlayer());
			if (pick != null && !pick.isEmpty())
				name = pick.getHoverName();
		}
		if (name == null) {
			String key = hitBlock.getBlock().getDescriptionId();
			if (I18n.exists(key)) {
				name = hitBlock.getBlock().getName();
			} else {
				ItemStack pick = accessor.getPickedResult();
				if (pick != null && !pick.isEmpty()) {
					name = pick.getHoverName();
				} else {
					name = new TextComponent(key);
				}
			}
		}
		tooltip.remove(OBJECT_NAME_TAG);
		tooltip.add(0, new TextComponent(String.format(config.getWailaConfig().getFormatting().getBlockName(), name.getString())).withStyle(Waila.CONFIG.get().getOverlay().getColor().getTitle().withItalic(true)), OBJECT_NAME_TAG);
	}

	public void setHit(BlockHitResult hitResult, BlockState hitBlock) {
		this.hitResult = hitResult;
		this.hitBlock = hitBlock;
		time = Util.getMillis();
	}

	private boolean validate() {
		return (Util.getMillis() - time) < 10;
	}

}
