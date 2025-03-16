package snownee.jade.addon.mixin.create;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.equipment.blueprint.BlueprintOverlayRenderer;

import net.minecraft.world.item.ItemStack;

@Mixin(value = BlueprintOverlayRenderer.class, remap = false)
public interface BlueprintOverlayRendererAccess {
	@Accessor
	static List<ItemStack> getResults() {
		throw new AssertionError();
	}
}
