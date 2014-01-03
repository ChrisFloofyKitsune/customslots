package chrisclark13.minecraft.customslots.inventory;

import java.util.ArrayList;

import chrisclark13.minecraft.customslots.helper.MultiSlotItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryMultiSlotItemGrid implements ISidedInventory {
    private GridSlot[] slots;
    private int width;
    private int height;
    
    public InventoryMultiSlotItemGrid(int width, int height) {
        this.width = width;
        this.height = height;
        
        slots = new GridSlot[width * height];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                slots[x + (y * width)] = new GridSlot(this, x + (y * width), x, y);
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
        return slots.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int slotId) {
        return slots[slotId].getItemStack();
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        
        ItemStack itemStack = getStackInSlot(slot);
        
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
                itemStack.stackSize = amount;
                return itemStack;
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
                return itemStack;
            }
        }
        
        return itemStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        
        if (getStackInSlot(slot) != null) {
            ItemStack itemStack = getStackInSlot(slot);
            setInventorySlotContents(slot, null);
            return itemStack;
        } else
            return null;
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        slots[i].setItemStack(itemstack);
        onInventoryChanged();
    }
    
    @Override
    public String getInvName() {
        return "Multi-Slot Item Inventory";
    }
    
    @Override
    public boolean isInvNameLocalized() {
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void onInventoryChanged() {
        for (GridSlot slot : slots) {
            slot.updateSlot();
        }
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }
    
    @Override
    public void openChest() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return slots[i].isItemStackValid(itemStack);
    }
    
    @Override
    public void closeChest() {
        // TODO Auto-generated method stub
        
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isGridSlotOpenAt(int x, int y) {
        return getGridSlotAt(x, y) != null ? getGridSlotAt(x, y).isOpen() : false;
    }
    
    public GridSlot getGridSlotAt(int x, int y) {
        if (!isCoordsInBounds(x, y)) {
            return null;
        } else {
            return slots[x + (y * width)];
        }
    }
    
    public GridSlot getGridSlot(int slotId) {
        return slots[slotId];
    }
    
    public boolean isCoordsInBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    public boolean isSpaceForItemAt(int x, int y, ItemStack itemStack) {
        SlotSignature sig = MultiSlotItemHelper.getSignature(itemStack);
        
        // If the signature is one by one, we just need to check one spot
        if (sig.isOneByOne() && isGridSlotOpenAt(x, y)) {
            return true;
        } else if (!isGridSlotOpenAt(x, y)) {
            // If it's not just one by one, we should start off by checking this
            // slot anyways
            return false;
        }
        
        // Start the serious checking, if even one slot is blocked then
        // we can't place the itemStack here.
        for (int row = 0; row < sig.getHeight(); row++) {
            for (int column = 0; column < sig.getWidth(); column++) {
                if (sig.isSlotDesignated(column, row)
                        && !isGridSlotOpenAt(x + column + sig.getRelativeLeft(),
                                y + row + sig.getRelativeTop())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Creates all the appropriate slots for the grid to a ContainerMultiSlotItem,
     * because the addSlotToContainer method is protected, you'll need to iterate through the list
     * and add all of the slots returned by this method.
     * 
     * @param container The ContainerMultiSlotItem to add the slots to.
     * @param displayX The x-coord of the top left corner of where the grid is going to be.
     * @param displayY The y-coord of the top left corener of where the grid is going to be.
     * @param slotWidth How wide each grid slot is going to be, used to space them out as well.
     * @param slotHeight how tall each grid slot is going to be, used to space them out as well.
     * @return
     */
    public ArrayList<SlotMultiSlotItem> createSlotsForContainer(ContainerMultiSlotItem container,
            int displayX, int displayY, int slotWidth, int slotHeight) {
        ArrayList<SlotMultiSlotItem> containerSlots = new ArrayList<SlotMultiSlotItem>(slots.length);
        
        for (GridSlot gridSlot : slots) {
            containerSlots.add(createSlotForContainer(container, gridSlot,
                    (gridSlot.getGridX() * slotWidth) + displayX,
                    (gridSlot.getGridY() * slotHeight) + displayY, slotWidth, slotHeight));
        }
        
        return containerSlots;
    }
    
    /**
     * Override this to use custom SlotMultiSlotItem slots for a container.<br/>
     * Called by
     * 
     * @param container
     * @param gridSlot
     * @param displayX
     * @param displayY
     * @param slotWidth
     * @param slotHeight
     * @return
     */
    protected SlotMultiSlotItem createSlotForContainer(ContainerMultiSlotItem container,
            GridSlot gridSlot, int displayX, int displayY, int slotWidth, int slotHeight) {
        return new SlotMultiSlotItem(container, gridSlot, displayX, displayY, slotWidth, slotHeight);
    }
    
    public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList list = tagCompound.getTagList("GridInventory");
        
        // Slots will auto-bind children as they are loaded.
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);
            short slot = tag.getShort("Slot");
            if (slot >= 0 && slot < getSizeInventory()) {
                if (tag.hasKey("Item")) {
                    slots[slot].setItemStack(ItemStack.loadItemStackFromNBT(tag
                            .getCompoundTag("Item")));
                }
            }
        }
    }
    
    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList list = new NBTTagList();
        
        // We just need to save the slots that actually have ItemStacks in them.
        for (int i = 0; i < this.getSizeInventory(); i++) {
            if (slots[i].getParentSlot() == null && slots[i].getItemStack() != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setShort("Slot", (short) i);
                tag.setCompoundTag("Item", slots[i].getItemStack().writeToNBT(new NBTTagCompound()));
                list.appendTag(tag);
            }
        }
        
        tagCompound.setTag("GridInventory", list);
    }
    
    public ItemStack[] getItemStacksInFilledSlots() {
        ItemStack[] stacks = new ItemStack[slots.length];
        
        for (int i = 0; i < slots.length; i++) {
            stacks[i] = slots[i].getParentSlot().getItemStack();
        }
        
        return stacks;
    }
    
    public GridSlot[] getSlots() {
        return slots;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for (GridSlot slot : slots) {
            if (slot.getParentSlot() == null) {
                list.add(slot.getSlotId());
            }
        }
        
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        
        return array;
    }
    
    @Override
    public boolean canInsertItem(int id, ItemStack itemStack, int side) {
        return isSpaceForItemAt(slots[id].getGridX(), slots[id].getGridY(), itemStack);
    }
    
    @Override
    public boolean canExtractItem(int id, ItemStack itemStack, int side) {
        return slots[id].getParentSlot() == null;
    }
}
