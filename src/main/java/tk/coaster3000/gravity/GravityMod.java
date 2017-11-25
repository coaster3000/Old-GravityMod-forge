package tk.coaster3000.gravity;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.gravity.command.GravityConfigCommand;
import tk.coaster3000.gravity.common.Config;
import tk.coaster3000.gravity.scheduler.PhysicsScheduler;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import static tk.coaster3000.gravity.GravityMod.*;

@Mod(modid = MODID, name = MODNAME, version = MODVERSION)
public class GravityMod {
	static final String MODID = "physicsmod";
	static final String MODNAME = "Physics Mod";

	static final String MODVERSION = "@mod_version@";

	@Mod.Instance(MODID)
	public static GravityMod instance;

	private Logger logger;

	private WorldListener worldListener;
	private BlockListener blockListener;
	private PhysicsHandler physicsHandler;

	private PhysicsScheduler physicsScheduler;


	public Logger getLogger() {
		return logger;
	}

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		this.logger = event.getModLog();

		Config.load(event);

		logger.info("Pre-Initialization Completed.");
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {

		physicsScheduler = new PhysicsScheduler();

		worldListener = new WorldListener(physicsScheduler);
		blockListener = new BlockListener(physicsScheduler);
		physicsHandler = new PhysicsHandler(physicsScheduler);


		EVENT_BUS.register(worldListener);
		EVENT_BUS.register(blockListener);
		EVENT_BUS.register(physicsHandler);


		logger.info("Initialization Completed.");
	}

	@Mod.EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(GravityConfigCommand.instance);
	}
}
