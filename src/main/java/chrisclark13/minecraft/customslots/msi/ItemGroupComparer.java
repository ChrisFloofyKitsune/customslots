package chrisclark13.minecraft.customslots.msi;

import net.minecraft.item.ItemStack;

public class ItemGroupComparer {
    public boolean canMerge(ItemGroup a, ItemGroup b) {
        ItemStack aStack = (a.getItemStacks().isEmpty()) ? null : a.getItemStacks().get(0);
        ItemStack bStack = (b.getItemStacks().isEmpty()) ? null : b.getItemStacks().get(0);
        
        return (aStack == null || bStack == null) ? false : (aStack.isItemEqual(bStack) && ItemStack.areItemStackTagsEqual(aStack, bStack));
    }
}
