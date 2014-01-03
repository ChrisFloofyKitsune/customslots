package chrisclark13.minecraft.customslots.inventory;

import java.util.ArrayList;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class ContainerCustomSlots extends Container {

	public ArrayList<Slot> normalSlots = new ArrayList<Slot>();
	public ArrayList<SlotCustom> customSlots = new ArrayList<SlotCustom>();
	
	@Override
	protected Slot addSlotToContainer(Slot slot) {
		if (slot instanceof SlotCustom) {
			customSlots.add((SlotCustom) slot);
		} else {
			normalSlots.add(slot);
		}
		
		return super.addSlotToContainer(slot);
	}

}
