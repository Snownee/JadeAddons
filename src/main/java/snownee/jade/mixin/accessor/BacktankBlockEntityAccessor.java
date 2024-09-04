package snownee.jade.mixin.accessor;

import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BacktankBlockEntity.class)
public interface BacktankBlockEntityAccessor {
	@Accessor(value = "capacityEnchantLevel", remap = false)
	int getCapacityEnchantLevel();
}
