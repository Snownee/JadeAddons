package snownee.jadeaddons.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;

@Mixin(BacktankBlockEntity.class)
public interface BacktankBlockEntityAccess {
	@Accessor("capacityEnchantLevel")
	int getCapacityEnchantLevel();
}
