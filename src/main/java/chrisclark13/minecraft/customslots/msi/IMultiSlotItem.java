package chrisclark13.minecraft.customslots.msi;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IMultiSlotItem {
	//public void registerImages(MultiSlotItemRegistry msiRegistry);
	/**
	 * Get the image to draw for this multiSlotItem based on itemStack;
	 * @param itemStack
	 * @return
	 */
	public ResourceLocation getImageResource(ItemStack itemStack);
	
	/**
	 * Get the slots this ItemStack fills up in the inventory</br>
	 * The returned string should be in this format:</br>
	 * <code>" # \n"</br>
	 * "#X#\n"</br>
	 * " # \n"</br></code>
	 * Empty slots are represented by spaces, the center of the multiSlotItem is represented by an X.</br>
	 * All other non-space non-'X' characters represent spaces filled by the item.
	 * @param itemStack itemStack is passed so you can differentiate based on metadata and NBT tags
	 * @return String in the above format
	 */
	public String getSlotSignature(ItemStack itemStack);
	
	
	/**
	 * Gets the color to use when drawing this image, if you don't want to color it at all
	 * just return 0xFFFFFF (white).</br>
	 * Ignores alpha information. (0xAARRGGBB) 
	 * @param itemStack
	 * @return A color packed into an integer. (0xRRGGBB)
	 */
	public int getImageColor(ItemStack itemStack);
}
