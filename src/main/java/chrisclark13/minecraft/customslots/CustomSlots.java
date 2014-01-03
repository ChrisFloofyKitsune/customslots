package chrisclark13.minecraft.customslots;

import chrisclark13.minecraft.customslots.helper.LogHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = CustomSlots.MOD_ID, name = "CustomSlots", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class CustomSlots {
	public static final String MOD_ID = "CustomSlots";

	@Instance(MOD_ID)
	public static CustomSlots instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init(event.getModLog());
	}

}
