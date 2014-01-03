package chrisclark13.minecraft.customslots.inventory;

import java.util.HashSet;
import java.util.logging.Level;

import chrisclark13.minecraft.customslots.helper.MultiSlotItemHelper;
import chrisclark13.minecraft.customslots.helper.LogHelper;
import net.minecraft.item.ItemStack;

public class GridSlot {
	private InventoryMultiSlotItemGrid grid;
	private int slotId;
	private int gridX;
	private int gridY;
	private boolean enabled;

	private ItemStack itemStack;

	// Info used in connecting to other slots
	private GridSlot parentSlot;
	private HashSet<GridSlot> childSlots;

	public GridSlot(InventoryMultiSlotItemGrid grid, int slotId,
			int gridX, int gridY) {

		this.grid = grid;
		this.slotId = slotId;
		this.gridX = gridX;
		this.gridY = gridY;
		this.enabled = true;

		this.itemStack = null;

		this.parentSlot = null;
		this.childSlots = new HashSet<GridSlot>();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		if (this.itemStack != itemStack) {
		    if (itemStack != null && !MultiSlotItemHelper.getSignature(itemStack).isOneByOne()) {
		        bindChildSlots(MultiSlotItemHelper.getSignature(itemStack));
		    }
		}
	    
	    this.itemStack = itemStack;
		
		this.updateSlot();
	}

	public int getSlotId() {
		return slotId;
	}

	public InventoryMultiSlotItemGrid getGrid() {
		return grid;
	}

	public int getGridX() {
		return gridX;
	}

	public int getGridY() {
		return gridY;
	}

	public GridSlot getParentSlot() {
		return parentSlot;
	}

	public GridSlot getParentSlotIfExists() {
	    return (parentSlot != null) ? parentSlot : this;
	}
	
	/**
	 * Use sparingly since changing this around may case weird crap to happen.</br>
	 * Detaches itself from it's existing parentSlot if it has one.
	 * 
	 * @param slot
	 *            New parentSlot
	 */
	public void setParentSlot(GridSlot slot) {
		if (slot == this || slot == parentSlot) {
			return;
		}
		
		if (parentSlot != null) {
			parentSlot.childSlots.remove(this);
		}

		parentSlot = slot;
		parentSlot.childSlots.add(this);
	}

	public HashSet<GridSlot> getChildSlots() {
		return childSlots;
	}

	/**
	 * Should be called each time setItemStack is called or whenever needed to
	 * make sure that all slots are up to date.</br> Makes sure that slots
	 * aren't locked when they shouldn't be
	 */
	public void updateSlot() {
		// Redirect update to parent slot.
		if (parentSlot != null) {
			parentSlot.updateSlot();

			// If a slot has a parentSlot and childrenSlots, something went
			// wrong!!
			if (!childSlots.isEmpty()) {
				LogHelper.log(Level.SEVERE, "A MultiSlotItemGridSlot has a parentSlot "
						+ "and childrenSlots! Something went wrong!!");
			}
		} else {
			if (itemStack == null && !childSlots.isEmpty()) {
				freeChildSlots();
			}
			
			if (itemStack != null && childSlots.isEmpty() && !MultiSlotItemHelper.getSignature(itemStack).isOneByOne()) {
				bindChildSlots(MultiSlotItemHelper.getSignature(itemStack));
			}
		}
	}

	// Bind the child slots specified in the signature to this slot
	private void bindChildSlots(SlotSignature sig) {
		
		if (!childSlots.isEmpty()) {
			freeChildSlots();
		}
			
		for (int row = 0; row < sig.getHeight(); row++) {
			for (int column = 0; column < sig.getWidth(); column++) {
				if (sig.isSlotDesignated(column, row)) {
					GridSlot slot = grid.getGridSlotAt(gridX
							+ column + sig.getRelativeLeft(),
							gridY + row + sig.getRelativeTop());
					if (slot == null) {
						LogHelper.log(Level.SEVERE, "A MSIGridSlot at (" + gridX + "," + gridY + ") attempted to bind a slot" +
								" that doesn't exist at (" + (gridX
										+ column + sig.getRelativeLeft()) + "," +
								(gridY + row + sig.getRelativeTop()) + ")!");
					} else if (slot != this) {
						slot.parentSlot = this;
						childSlots.add(slot);
					}
				}
			}
		}
	}

	// Free the child slots that have been bound to this slot
	private void freeChildSlots() {
		for (GridSlot slot : childSlots) {
			slot.parentSlot = null;
		}

		childSlots.clear();
	}

	public boolean isItemStackValid(ItemStack itemStack) {
		if (!enabled) {
		    return false;
		} else if (getItemStack() != null && getItemStack().isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(getItemStack(), itemStack)) {
			return true;
		} else {
			return grid.isSpaceForItemAt(gridX, gridY, itemStack);
		}
	}
	
	public boolean isOpen() {
		return (parentSlot == null && itemStack == null && enabled);
	}

}
