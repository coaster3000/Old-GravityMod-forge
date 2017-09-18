package tk.coaster3000.gravity;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import static tk.coaster3000.gravity.GravityMod.*;

@Mod(modid = MODID, name = MODNAME, version = MODVERSION)
public class GravityMod {
	public static final String MODID = "physicsmod";
	public static final String MODNAME = "Physics Mod";

	public static final String MODVERSION = "@mod_version@";

	static GravityMod instance = null;

	private static void setInstance(GravityMod instance) {
		GravityMod.instance = instance;
	}

	public static GravityMod getInstance() {
		return GravityMod.instance;
	}

	private Logger logger;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		this.logger = event.getModLog();
		logger.info("Pre-Initialization Completed.");
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		logger.info("Initialization Completed.");
	}
}
