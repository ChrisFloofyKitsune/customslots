package chrisclark13.minecraft.customslots.inventory;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMultiSlotItem extends SlotCustom {
    
    protected ContainerMultiSlotItem container;
    protected GridSlot gridSlot;
    public boolean enabled = true;
    
    public SlotMultiSlotItem(ContainerMultiSlotItem container, GridSlot gridSlot, int displayX,
            int displayY, int width, int height) {
    	//IInventory inventory, int slotIndex, float xPosition, float yPosition, float width, float height
        super(gridSlot.getGrid(), gridSlot.getSlotId(), displayX, displayY, width, height);
        
        this.container = container;
        this.gridSlot = gridSlot;
        
        this.padding = 0;
    }
    
    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return gridSlot.isItemStackValid(par1ItemStack);
    }
    
    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        gridSlot.updateSlot();
    }
    
    public GridSlot getGridSlot() {
        return gridSlot;
    }
    
    /**
     * Gets the parent slot of this slot. If there is no parent slot, it returns
     * itself.</br> This is so you don't have to check ahead of time if it has a
     * parent slot before deciding whether or not to call this function.
     * 
     * @return
     */
    public SlotMultiSlotItem getParentSlotIfExists() {
        if (gridSlot.getParentSlot() == null) {
            return this;
        } else {
            // Based on that all slots in the Grid are consecutive; this gets
            // the
            // parent slot using the difference between this slot's
            // slotNumber and its gridSlot's id as an offset
            return (SlotMultiSlotItem) container.inventorySlots.get(gridSlot.getParentSlot()
                    .getSlotId() + (this.slotNumber - gridSlot.getSlotId()));
        }
    }
    
    /**
     * Gets all the slots that are linked to this one</br> Does not include the
     * calling slot since it's assumed you already have a reference to this
     * slot.
     * 
     * @return
     */
    public Set<SlotMultiSlotItem> getLinkedSlots() {
        HashSet<SlotMultiSlotItem> set = new HashSet<SlotMultiSlotItem>();
        
        HashSet<GridSlot> childSlots;
        
        if (gridSlot.getParentSlot() == null) {
            childSlots = gridSlot.getChildSlots();
        } else {
            set.add((SlotMultiSlotItem) container.inventorySlots.get(gridSlot.getParentSlot()
                    .getSlotId() + (this.slotNumber - gridSlot.getSlotId())));
            childSlots = gridSlot.getParentSlot().getChildSlots();
        }
        
        for (GridSlot slot : childSlots) {
            if (slot != gridSlot) {
                set.add((SlotMultiSlotItem) container.inventorySlots.get(slot.getSlotId()
                        + (this.slotNumber - gridSlot.getSlotId())));
            }
        }
        
        return set;
    }
    
    /**
     * Gets all slots that are linked to this slot, including this one.
     * 
     * @return
     */
    public Set<SlotMultiSlotItem> getLinkedSlotsIncludingThis() {
        Set<SlotMultiSlotItem> set = this.getLinkedSlots();
        set.add(this);
        return set;
    }
    
    @Override
    public boolean func_111238_b() {
    	return enabled;
    }
}
