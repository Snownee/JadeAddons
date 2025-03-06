package snownee.jade.addon.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;

@Mixin(value = BacktankBlockEntity.class, remap = false)
public interface BacktankBlockEntityAccess {
	@Accessor("capacityEnchantLevel")
	int getCapacityEnchantLevel();
}
