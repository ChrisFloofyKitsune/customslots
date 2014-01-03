package chrisclark13.minecraft.customslots.asm;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class CSAccessTransfomer extends AccessTransformer {
	public CSAccessTransfomer() throws IOException {
		super("customslots_at.cfg");
	}

}
