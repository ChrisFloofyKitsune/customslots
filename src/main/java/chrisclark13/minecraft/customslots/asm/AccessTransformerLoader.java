package chrisclark13.minecraft.customslots.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class AccessTransformerLoader implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "chrisclark13.minecraft.customslots.asm.CSAccessTransformer" };
	}

	@Override
	public String getModContainerClass() {
		// No op
		return null;
	}

	@Override
	public String getSetupClass() {
		// No op
		return null;
	}

	@Override
	public void injectData(Map<String, Object> arg0) {
		// No op
	}

}
