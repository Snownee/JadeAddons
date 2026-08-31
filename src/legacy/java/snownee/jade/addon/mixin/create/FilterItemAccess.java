package snownee.jade.addon.mixin.create;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.logistics.filter.FilterItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Mixin(value = FilterItem.class, remap = false)
public interface FilterItemAccess {
	@Invoker
	List<Component> callMakeSummary(ItemStack filter);
}
