package snownee.jadeaddons.create;

import com.zurrtum.create.infrastructure.fluids.FluidInventory;
import com.zurrtum.create.infrastructure.fluids.FluidStack;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

final class FluidInventoryResourceHandler implements ResourceHandler<FluidResource> {
	private final FluidInventory inventory;

	FluidInventoryResourceHandler(FluidInventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public int size() {
		return inventory.size();
	}

	@Override
	public FluidResource getResource(int index) {
		FluidStack stack = inventory.getStack(index);
		if (stack.isEmpty()) {
			return FluidResource.EMPTY;
		}
		return FluidResource.of(stack.getFluid(), stack.getComponentChanges());
	}

	@Override
	public long getAmountAsLong(int index) {
		return inventory.getStack(index).getAmount();
	}

	@Override
	public long getCapacityAsLong(int index, FluidResource resource) {
		FluidStack stack = inventory.getStack(index);
		if (stack.isEmpty()) {
			return inventory.getMaxAmountPerStack();
		}
		return inventory.getMaxAmount(stack);
	}

	@Override
	public boolean isValid(int index, FluidResource resource) {
		return true;
	}

	@Override
	public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
		return 0;
	}

	@Override
	public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
		return 0;
	}
}
