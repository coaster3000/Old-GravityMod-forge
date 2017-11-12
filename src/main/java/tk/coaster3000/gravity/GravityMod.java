package tk.coaster3000.gravity;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.gravity.util.PhysicsUtil;

import static tk.coaster3000.gravity.GravityMod.*;

@Mod(modid = MODID, name = MODNAME, version = MODVERSION)
public class GravityMod {
	static final String MODID = "physicsmod";
	static final String MODNAME = "Physics Mod";

	static final String MODVERSION = "@mod_version@";

	private static GravityMod instance = null;

	private Logger logger;


	public Logger getLogger() {
		return logger;
	}

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		setInstance(this);
		this.logger = event.getModLog();
		logger.info("Pre-Initialization Completed.");
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		logger.info("Initialization Completed.");
	}

	public static GravityMod getInstance() {
		return GravityMod.instance;
	}

	private static void setInstance(GravityMod instance) {
		GravityMod.instance = instance;
	}
}
