package snownee.jade.addon.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;

@Mixin(BacktankBlockEntity.class)
public interface BacktankBlockEntityAccess {
	@Accessor(value = "capacityEnchantLevel", remap = false)
	int getCapacityEnchantLevel();
}
