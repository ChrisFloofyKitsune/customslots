package chrisclark13.minecraft.customslots.msi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import net.minecraft.util.ResourceLocation;
import chrisclark13.minecraft.customslots.helper.LogHelper;


/**
 * Registry for Items that don't implement IMultiSlotItem, like vanilla items and other mod items</br>
 * Should only be used after checking to see if an Item implements IMultiSlotItem or not because if it
 * does that item will likely return better information based on ItemStack data rather than an
 * unlocalized name.
 * 
 * @author ChrisClark13
 *
 */
public class MultiSlotItemRegistry {
	public static ConcurrentHashMap<String, Entry> entryMap = new ConcurrentHashMap<String, Entry>();
	
	public static void registerUnlocalizedName(String unlocalizedName, ResourceLocation imageResource, String slotSingature) {
	    LogHelper.log(Level.FINE, "Registering MSI: " + unlocalizedName + " With image path: " + imageResource + " and SlotSignature " + slotSingature.replaceAll("\n", "\\n"));
		//System.out.println("Registering: " + unlocalizedName + " With: " + imagePath + ", " + slotSingature.replaceAll("\n", "\\n"));
	    entryMap.put(unlocalizedName, new Entry(imageResource, slotSingature));
	}
	
	public static ResourceLocation getImageResource(String unlocalizedName) {
		return entryMap.containsKey(unlocalizedName) ? entryMap.get(unlocalizedName).imageResource : null;
	}
	
	public static String getSignatureString(String unlocalizedName) {
		return entryMap.containsKey(unlocalizedName) ? entryMap.get(unlocalizedName).slotSingature : null;
	}
	
	public static boolean hasEntry(String unlocalizedName) {
	    return entryMap.containsKey(unlocalizedName);
	}
	
	private MultiSlotItemRegistry() {};
	
	private static class Entry {
		ResourceLocation imageResource;
		String slotSingature;
		
		private Entry(ResourceLocation imageResource, String slotSingature) {
			this.imageResource = imageResource;
			this.slotSingature = slotSingature;
		}
	}
}
