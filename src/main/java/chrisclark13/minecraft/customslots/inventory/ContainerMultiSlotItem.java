package chrisclark13.minecraft.customslots.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerMultiSlotItem extends ContainerCustomSlots {

	private InventoryMultiSlotItemGrid grid;

	public ContainerMultiSlotItem(InventoryMultiSlotItemGrid grid) {
		this.grid = grid;
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return !(slot instanceof SlotMultiSlotItem);
	}

	@Override
	public ItemStack slotClick(int slotId, int clickAction, int slotAction,
			EntityPlayer player) {

		// We only want to handle clicking for SlotMultiSlotItem slots
		if (slotId <= 0 || slotId > inventorySlots.size()
				|| !(inventorySlots.get(slotId) instanceof SlotMultiSlotItem)) {
			return super.slotClick(slotId, clickAction, slotAction, player);
		}

		// If this slot is a SlotMSI slot, then call it again with the right
		// slot to do things on
		Slot slot = ((SlotMultiSlotItem) inventorySlots.get(slotId))
				.getParentSlotIfExists();
		return super
				.slotClick(slot.slotNumber, clickAction, slotAction, player);

	}

	@Override
	/**
	 * transferStackInSlot method ready to go to work with SlotMultiSlotItems</br>
	 * If you make a subclass with additional inventory beyond the MSI grid (which you most likely will), you'll want to copy this and 
	 * change grid.getSizeInventory() to yourInventoryHere.getSizeInventory()
	 */
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer,
			int slotIndex) {

		ItemStack newItemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);

		if (slot != null) {
			if (slot instanceof SlotMultiSlotItem) {
				slot = ((SlotMultiSlotItem) slot).getParentSlotIfExists();
			}
			if (slot.getHasStack()) {
				ItemStack itemStack = slot.getStack();
				newItemStack = itemStack.copy();

				if (slotIndex < grid.getSizeInventory()) {
					if (!this.mergeItemStack(itemStack,
							grid.getSizeInventory(), inventorySlots.size(),
							true))
						return null;
				} else if (!this.mergeItemStack(itemStack, 0,
						grid.getSizeInventory(), false))
					return null;

				if (itemStack.stackSize == 0) {
					slot.putStack((ItemStack) null);
				}

				slot.onSlotChanged();
			}
		}

		return newItemStack;
	}

	public InventoryMultiSlotItemGrid getGrid() {
		return grid;
	}

	/**
	 * Merges provided ItemStack with the first available one in the
	 * container/player inventory</br> Same as before except that it now checks
	 * slot.isItemValid before placing a stack into a slot and it respects
	 * inventory and slot stack size limits.
	 */
	protected boolean mergeItemStack(ItemStack sourceStack, int startIndex,
			int endIndex, boolean reverseOrder) {
		boolean success = false;
		int k = startIndex;

		if (reverseOrder) {
			k = endIndex - 1;
		}

		Slot slot;
		ItemStack destStack;

		if (sourceStack.isStackable()) {
			while (sourceStack.stackSize > 0
					&& (!reverseOrder && k < endIndex || reverseOrder
							&& k >= startIndex)) {
				slot = (Slot) this.inventorySlots.get(k);
				destStack = slot.getStack();
				int stackLimit = Math.min(
						sourceStack.getMaxStackSize(),
						Math.min(slot.getSlotStackLimit(),
								slot.inventory.getInventoryStackLimit()));

				if (destStack != null
						&& destStack.itemID == sourceStack.itemID
						&& (!sourceStack.getHasSubtypes() || sourceStack
								.getItemDamage() == destStack.getItemDamage())
						&& ItemStack.areItemStackTagsEqual(sourceStack,
								destStack)) {

					int l = destStack.stackSize + sourceStack.stackSize;

					if (l <= stackLimit) {
						sourceStack.stackSize = 0;
						destStack.stackSize = l;
						slot.onSlotChanged();
						success = true;
					} else if (destStack.stackSize < stackLimit) {
						sourceStack.stackSize -= stackLimit
								- destStack.stackSize;
						destStack.stackSize = stackLimit;
						slot.onSlotChanged();
						success = true;
					}
				}

				if (reverseOrder) {
					--k;
				} else {
					++k;
				}
			}
		}

		if (sourceStack.stackSize > 0) {
			if (reverseOrder) {
				k = endIndex - 1;
			} else {
				k = startIndex;
			}

			while (!reverseOrder && k < endIndex || reverseOrder
					&& k >= startIndex) {
				slot = (Slot) this.inventorySlots.get(k);
				destStack = slot.getStack();

				if (destStack == null && slot.isItemValid(sourceStack)) {
					int stackLimit = Math.min(slot.getSlotStackLimit(),
							slot.inventory.getInventoryStackLimit());

					ItemStack copyStack = sourceStack.copy();
					copyStack.stackSize = Math.min(sourceStack.stackSize,
							stackLimit);
					slot.putStack(copyStack);
					slot.onSlotChanged();

					sourceStack.stackSize = Math.max(0, sourceStack.stackSize
							- stackLimit);
					success = true;
					break;
				}

				if (reverseOrder) {
					--k;
				} else {
					++k;
				}
			}
		}

		return success;
	}
}
