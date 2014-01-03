package chrisclark13.minecraft.customslots.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {
	private static Logger cslogger;
	
	private LogHelper(Logger logger) {
	}
	
	public static void init(Logger logger) {
		cslogger = logger;
	}
	
	public static void log(Level level, String msg) {
		cslogger.log(level, msg);
	}
}
