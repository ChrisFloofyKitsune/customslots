package chrisclark13.minecraft.customslots.msi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.item.ItemStack;
import chrisclark13.minecraft.customslots.helper.MultiSlotItemHelper;
import chrisclark13.minecraft.customslots.inventory.GridSlot;
import chrisclark13.minecraft.customslots.inventory.InventoryMultiSlotItemGrid;
import chrisclark13.minecraft.customslots.inventory.SlotSignature;
import chrisclark13.minecraft.customslots.helper.LogHelper;

/**
 * A group of items in a MultiSlotItemGrid, designed to be used in grids where you
 * want to get all the items of a same type that are touching each other or want to
 * check if items are touching each other.
 * 
 * @author ChrisClark13
 * 
 */
public class ItemGroup {
    private boolean[] positions;
    private int left = 0;
    private int top = 0;
    private int width = 0;
    private int height = 0;
    private InventoryMultiSlotItemGrid grid;
    private LinkedList<GridSlot> parentSlots;
    
    public static List<ItemGroup> createItemGroupsFromGrid(InventoryMultiSlotItemGrid grid) {
        return createItemGroupsFromGrid(grid, new ItemGroupComparer());
    }
    
    public static List<ItemGroup> createItemGroupsFromGrid(InventoryMultiSlotItemGrid grid, ItemGroupComparer comparer) {
        ArrayList<ItemGroup> itemGroups = new ArrayList<ItemGroup>();
        
        for (GridSlot slot : grid.getSlots()) {
            if (slot.getItemStack() != null) {
                itemGroups.add(new ItemGroup(slot));
            }
        }
        
        return mergeGroups(itemGroups, comparer);
    }
    
    public static List<ItemGroup> mergeGroups(List<ItemGroup> itemGroups) {
        return mergeGroups(itemGroups, new ItemGroupComparer());
    }
    
    public static List<ItemGroup> mergeGroups(List<ItemGroup> itemGroups, ItemGroupComparer comparer) {
        ArrayList<ItemGroup> list = new ArrayList<ItemGroup>(itemGroups);
        ArrayList<ItemGroup> groupsToRemove = new ArrayList<ItemGroup>();
        
        Comparator<ItemGroup> c = new Comparator<ItemGroup>() {
            
            @Override
            public int compare(ItemGroup o1, ItemGroup o2) {
                if (o1.top < o2.top) {
                    return -1;
                } else if (o1.top > o2.top) {
                    return 1;
                } else if (o1.left < o2.left) {
                    return -1;
                } else if (o1.left > o2.left){
                    return 1;
                }
                
                return 0;
            }
        };
        
        Collections.sort(list, c);
        
        int prevSize = list.size();
        
        for (int i = 0; i < itemGroups.size() - 1; i++) {
            for (int j = i + 1; j < itemGroups.size(); j++) {
                ItemGroup a = list.get(i);
                ItemGroup b = list.get(j);
                
                if (groupsToRemove.contains(a) || groupsToRemove.contains(b) || a == b) {
                    continue;
                }
                
                if (comparer.canMerge(a, b) && a.isItemGroupTouching(b)) {
                    a.joinItemGroup(b);
                    groupsToRemove.add(b);
                }
            }
        }
        
        list.removeAll(groupsToRemove);
        
        //Recursion to make sure we got everything
        if (prevSize != list.size() && list.size() > 1) {
            return mergeGroups(list, comparer);
        }
        
        return list;
    }
    
    public ItemGroup(GridSlot slot) {
        if (slot != null) {
            slot = slot.getParentSlotIfExists();
            
            this.grid = slot.getGrid();
            this.positions = new boolean[grid.getWidth() * grid.getHeight()];
            
            SlotSignature sig = MultiSlotItemHelper.getSignature(slot.getItemStack());
            
            this.left = slot.getGridX() + sig.getRelativeLeft();
            this.top = slot.getGridY() + sig.getRelativeTop();
            this.width = sig.getWidth();
            this.height = sig.getHeight();
            
            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    if (sig.isSlotDesignated(column, row)) {
                        setFilledAtPosition(left + column, top + row);
                    }
                }
            }
            
            parentSlots = new LinkedList<GridSlot>();
            parentSlots.add(slot);
        } else {
            grid = null;
            positions = null;
            left = top = width = height = 0;
            parentSlots = null;
        }
        
    }
    
    public boolean isItemInSlotTouching(GridSlot slot) {
        slot = slot.getParentSlotIfExists();
        SlotSignature sig = MultiSlotItemHelper.getSignature(slot.getItemStack());
        
        int oLeft = slot.getGridX() + sig.getRelativeLeft();
        int oTop = slot.getGridY() + sig.getRelativeTop();
        int oRight = oLeft + sig.getWidth();
        int oBottom = oTop + sig.getHeight();
        
        int right = left + width;
        int bottom = top + height;
        
        // Step 1: AABB checking, fast fail condition
        if (right + 1 < oLeft || bottom + 1 < oTop || left - 1 > oRight || top - 1 > oBottom) {
            return false;
        }
        
        for (int y = top; y <= bottom; y++) {
            for (int x = left; x <= right; x++) {
                if (isFilledAtPosition(x, y)) {
                    // Step 2: Slot by Slot AABB checking since we're checking if 
                    // the other object is at least next to this slot and we don't want
                    // to same axis check every single slot with every single slot in the other object
                    if (!(x + 1 < oLeft || y + 1 < oTop || x - 1 > oRight || y - 1 > oBottom)) {
                        // Step 3: Check each slot of the other shape with this slot
                        for (int row = 0; row < sig.getHeight(); row++) {
                            for (int column = 0; column < sig.getWidth(); column++) {
                                // Step 3.1: Check to make sure they share at least one axis
                                if (sig.isSlotDesignated(column, row)) {
                                    int ox = column + oLeft;
                                    int oy = row + oTop;
                                    
                                    if (x == ox) { // Step 3.2a: Y axis distance check
                                        if (((y > oy) && (y - oy <= 1)) || ((oy > y) && (oy - y <= 1))) {
                                            return true;
                                        }
                                    } else if (y == oy) { // Step 3.2b: X axis distance check
                                        if (((x > ox) && (x - ox <= 1)) || ((ox > x) && (ox - x <= 1))) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean isItemGroupTouching(ItemGroup other) {
        
        int oLeft = other.getLeft();
        int oTop = other.getTop();
        int oRight = other.getRight();
        int oBottom = other.getBottom();
        
        int right = this.getRight();
        int bottom = this.getBottom();
        
        // Step 1: AABB checking, fast fail condition
        if (right + 1 < oLeft || bottom + 1 < oTop || left - 1 > oRight || top - 1 > oBottom) {
            //LogHelper.log(Level.INFO, "AABBs not touching!");
            return false;
        }
        
        //LogHelper.log(Level.INFO, "AABBs touching!");
        
        for (int y = top; y <= bottom; y++) {
            for (int x = left; x <= right; x++) {
                if (isFilledAtPosition(x, y)) {
                    // Step 2: Slot by Slot AABB checking since we're checking if 
                    // the other object is at least next to this slot and we don't want
                    // to same axis check every single slot with every single slot in the other object
                    if (!(x + 1 < oLeft || y + 1 < oTop || x - 1 > oRight || y - 1 > oBottom)) {
                        // Step 3: Check each slot of the other shape with this slot
                        for (int oy = oTop; oy <= oBottom; oy++) {
                                for (int ox = oLeft; ox <= oRight; ox++) {
                                // Step 3.1: Check to make sure they share at least one axis
                                if (other.isFilledAtPosition(ox, oy)) {
                                    
                                    if (x == ox) { // Step 3.2a: Y axis distance check
                                        if (((y > oy) && (y - oy <= 1)) || ((oy > y) && (oy - y <= 1))) {
                                            //LogHelper.log(Level.INFO, "Y-Axis Touch @ (" + x + ", " + y + ") & (" + ox + ", " + oy + ")");
                                            return true;
                                        }
                                    } else if (y == oy) { // Step 3.2b: X axis distance check
                                        if (((x > ox) && (x - ox <= 1)) || ((ox > x) && (ox - x <= 1))) {
                                            //LogHelper.log(Level.INFO, "X-Axis Touch @ (" + x + ", " + y + ") & (" + ox + ", " + oy + ")");
                                            return true;
                                        }
                                    }
                                    
                                    //LogHelper.log(Level.INFO, "No Touch @ (" + x + ", " + y + ") & (" + ox + ", " + oy + ")");
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public void joinItemInGridSlot(GridSlot slot) {
        slot = slot.getParentSlotIfExists();
        SlotSignature sig = MultiSlotItemHelper.getSignature(slot.getItemStack());
        
        int oLeft = slot.getGridX() + sig.getRelativeLeft();
        int oTop = slot.getGridY() + sig.getRelativeTop();
        
        for (int row = 0; row < sig.getHeight(); row++) {
            for (int column = 0; column < sig.getWidth(); column++) {
                if (sig.isSlotDesignated(column, row)) {
                    setFilledAtPosition(oLeft + column, oTop + row);
                }
            }
        }
        
        parentSlots.add(slot);
    }
    
    public void joinItemGroup(ItemGroup other) {
        for (int y = other.getTop(); y <= other.getBottom(); y++) {
            for (int x = other.getLeft(); x <= other.getRight(); x++) {
                if (other.isFilledAtPosition(x, y)) {
                    setFilledAtPosition(x, y);
                }
            }
        }
        
        parentSlots.addAll(other.getParentSlots());
    }
    
    protected void setFilledAtPosition(int gridX, int gridY) {
        if (gridX < 0 || gridX >= grid.getWidth() || gridY < 0 || gridY >= grid.getHeight()) {
            LogHelper.log(Level.SEVERE, this.toString() + " attempted to mark non-existant position (" + gridX + "," + gridY + ")");
            return;
        }
        
        if (gridX < left) {
            width += left - gridX;
            left = gridX;
        }
        
        if (gridX > getRight()) {
            width += gridX - getRight();
        }
        
        if (gridY < top) {
            height += top - gridY;
            top = gridY;
        }
        
        if (gridY > getBottom()) {
            height += gridY - getBottom();
        }
        
        
        positions[gridX + (gridY * grid.getWidth())] = true;
    }
    
    public boolean isFilledAtPosition(int gridX, int gridY) {
        if (gridX < 0 || gridX >= grid.getWidth() || gridY < 0 || gridY >= grid.getHeight()) {
            LogHelper.log(Level.SEVERE, this.toString() + " attempted to get non-existant position (" + gridX + "," + gridY + ")");
            return false;
        }
        
        return positions[gridX + (gridY * grid.getWidth())];
    }
    
    public List<GridSlot> getParentSlots() {
        return parentSlots;
    }
    
    public List<GridSlot> getSlots() {
        LinkedList<GridSlot> list = new LinkedList<GridSlot>();
        
        for (GridSlot slot : parentSlots) {
            list.add(slot);
            for (GridSlot childSlot : slot.getChildSlots()) {
                list.add(childSlot);
            }
        }
        
        return list;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }
    
    public int getRight() {
        return left + width - 1;
    }
    
    public int getBottom() {
        return top + height - 1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public InventoryMultiSlotItemGrid getGrid() {
        return grid;
    }
    
    public List<ItemStack> getItemStacks() {
        LinkedList<ItemStack> list = new LinkedList<ItemStack>();
        
        for (GridSlot slot : parentSlots) {
            if (slot.getItemStack() != null) {
                list.add(slot.getItemStack());
            }
        }
        
        return list;
    }
}
