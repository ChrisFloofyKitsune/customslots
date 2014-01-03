package chrisclark13.minecraft.customslots.helper;

import chrisclark13.minecraft.customslots.inventory.SlotSignature;
import chrisclark13.minecraft.customslots.msi.IMultiSlotItem;
import chrisclark13.minecraft.customslots.msi.MultiSlotItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MultiSlotItemHelper {
	public static boolean hasSignature(ItemStack itemStack) {
		if (itemStack.getItem() instanceof IMultiSlotItem) {
			return true;
		} else {
			String unlocalizedName = itemStack.getUnlocalizedName();
			return (MultiSlotItemRegistry.getSignatureString(unlocalizedName) != null);
		}
	}
	
	public static SlotSignature getSignature(ItemStack itemStack) {
		String signature = "X";
		if (itemStack.getItem() instanceof IMultiSlotItem) {
			signature = ((IMultiSlotItem)itemStack.getItem()).getSlotSignature(itemStack);
		} else if (MultiSlotItemRegistry.getSignatureString(itemStack.getUnlocalizedName()) != null) {
			signature = MultiSlotItemRegistry.getSignatureString(itemStack.getUnlocalizedName());
		}
		
		
		return SlotSignature.getFromString(signature);
	}
	
	public static ResourceLocation getImageResource(ItemStack itemStack) {
		if (itemStack.getItem() instanceof IMultiSlotItem) {
			return ((IMultiSlotItem) itemStack.getItem()).getImageResource(itemStack);
		} else {
			return MultiSlotItemRegistry.getImageResource(itemStack.getUnlocalizedName());
		}
	}
	
	public static int getImageColor(ItemStack itemStack) {
	    if (itemStack.getItem() instanceof IMultiSlotItem) {
	        return ((IMultiSlotItem) itemStack.getItem()).getImageColor(itemStack);
	    } else {
	        return 0xFFFFFF;
	    }
	}
}
