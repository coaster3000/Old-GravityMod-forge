/*
 * Copyright 2018 Coaster3000 (Christopher Krier)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package tk.coaster3000.gravity;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import static tk.coaster3000.gravity.GravityMod.MODID;
import static tk.coaster3000.gravity.GravityMod.MODNAME;
import static tk.coaster3000.gravity.GravityMod.MODVERSION;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.gravity.command.GravityCommand;
import tk.coaster3000.gravity.common.Config;
import tk.coaster3000.gravity.scheduler.PhysicsScheduler;



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

	/**
	 * Retrieves the logger assigned to this mod.
	 * @return Logger instance
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Called on FMLPreInitializationEvent to start the pre-initialization process of the mod.
	 * @param event the FMLPreInitializationEvent
	 */
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		this.logger = event.getModLog();
		Config.load(event);
	}

	/**
	 * Called on FMLInitializationEvent to start the initialization process of the mod.
	 * @param event the FMLInitializationEvent
	 */
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

	/**
	 * Called on FMLServerStartingEvent to register mods commands.
	 * @param event the FMLServerStartingEvent
	 */
	@Mod.EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(GravityCommand.instance);
	}
}
