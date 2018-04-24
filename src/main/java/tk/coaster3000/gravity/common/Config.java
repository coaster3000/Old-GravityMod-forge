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
package tk.coaster3000.gravity.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Config {
	private Config() {}


	static Configuration configFile;

	private static final String TOGGLES = "Enable-Disable";

	private static final String LIMITS = "Limits";

	private static final String LISTS = "Lists";

	private static Map<String, Supplier<?>> configRetrieval = new HashMap<>();
	private static Map<String, Consumer<?>> configSetters = new HashMap<>();
	private static Map<String, Class> configTypes = new HashMap<>();


	/*
	 * Dynamic Configuration
	 */

	public static Map<String, Supplier<?>> getRetrievers() { return Collections.unmodifiableMap(configRetrieval); }

	public static Map<String, Consumer<?>> getSetters() { return Collections.unmodifiableMap(configSetters); }

	public static Map<String, Class> getKeyClass() { return Collections.unmodifiableMap(configTypes); }

	/*
	 * KEYS
	 */

	private static final String maxFellTasksPerTickKey = "Max Fell Tasks Per Tick";

	private static final String maxCalculationTasksPerTickKey = "Max Calculation Tasks Per Tick";

	private static final String blockPhysicsWhitelistKey = "Block Physics Whitelist";

	private static final String blockPhysicsBlacklistKey = "Block Physics Blacklist";

	static {
		String key = "maxPhysicsCalculationTasks";
		configRetrieval.put(key, () -> Config.maxPhysicsCalculationTasks);
		configSetters.put(key, (Integer i) -> Config.maxPhysicsCalculationTasks = i);
		configTypes.put(key, Integer.class);

		key = "maxPhysicsFellTasks";
		configRetrieval.put(key, () -> Config.maxPhysicsFellTasks);
		configSetters.put(key, (Integer i) -> Config.maxPhysicsFellTasks = i);
		configTypes.put(key, Integer.class);

		key = "blockPhysicsWhitelist";
		configRetrieval.put(key, () -> Arrays.copyOf(Config.blockPhysicsWhitelist, Config.blockPhysicsWhitelist.length));
		configSetters.put(key, (Collection<String> s) -> Config.blockPhysicsWhitelist = (String[]) s.toArray());
		configTypes.put(key, Collection.class);

		key = "blockPhysicsBlacklist";
		configRetrieval.put(key, () -> Arrays.copyOf(Config.blockPhysicsBlacklist, Config.blockPhysicsBlacklist.length));
		configSetters.put(key, (Collection<String> s) -> Config.blockPhysicsBlacklist = (String[]) s.toArray());
		configTypes.put(key, Collection.class);
	}

	/**
	 * Called upon initial loading procedure of mod.
	 * @param event involved in the loading procedure
	 */
	public static void load(FMLPreInitializationEvent event) {
		configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.2", false);
		configFile.load();

		syncConfig();
	}

	/**
	 * Saves the configuration in memory, to the configuration file for the mod.
	 * @return true if any changes were found and saved.
	 */
	public static boolean saveConfig() {
		configFile.get(LIMITS, maxCalculationTasksPerTickKey, maxPhysicsCalculationTasks, "Determines how many physics calculation tasks are allowed to execute per world per tick.", 1, Integer.MAX_VALUE).set(maxPhysicsCalculationTasks);
		configFile.get(LIMITS, maxFellTasksPerTickKey, maxPhysicsFellTasks, "Determines how many physics block falling tasks can execute per world per tick.", 1, Integer.MAX_VALUE).set(maxPhysicsFellTasks);
		configFile.get(LISTS, blockPhysicsWhitelistKey, blockPhysicsWhitelist, "Determines what blocks are never ignored in physics checks. Anything listed here will go against default programmed behaviour.").set(blockPhysicsWhitelist);
		configFile.get(LISTS, blockPhysicsBlacklistKey, blockPhysicsBlacklist, "Determines what blocks are always ignored in physics checks. Anything listed here will go against default programmed behaviour.").set(blockPhysicsBlacklist);

		boolean changed = false;
		if (configFile.hasChanged()) {
			configFile.save();
			changed = true;
		}

		return changed;
	}

	/**
	 * Loads and synchronises the configuration settings from the configuration file for the mod.
	 * @return true if any changes were saved back to file.
	 */
	public static boolean syncConfig() {
		maxPhysicsCalculationTasks = configFile.get(LIMITS, maxCalculationTasksPerTickKey, maxPhysicsCalculationTasks, "Determines how many physics calculation tasks are allowed to execute per world per tick.", 1, Integer.MAX_VALUE).getInt(maxPhysicsCalculationTasks);
		maxPhysicsFellTasks = configFile.get(LIMITS, maxFellTasksPerTickKey, maxPhysicsFellTasks, "Determines how many physics block falling tasks can execute per world per tick.", 1, Integer.MAX_VALUE).getInt(maxPhysicsFellTasks);
		blockPhysicsWhitelist = configFile.get(LISTS, blockPhysicsWhitelistKey, blockPhysicsWhitelist, "Determines what blocks are never ignored in physics checks. Anything listed here will go against default programmed behaviour.").getStringList();
		blockPhysicsBlacklist = configFile.get(LISTS, blockPhysicsBlacklistKey, blockPhysicsBlacklist, "Determines what blocks are always ignored in physics checks. Anything listed here will go against default programmed behaviour.").getStringList();

		boolean changed = false;
		if (configFile.hasChanged()) {
			configFile.save();
			changed = true;
		}

		return changed;
	}

	/*
	 * Settings and values
	 */

	public static int maxPhysicsCalculationTasks = 100;

	public static int maxPhysicsFellTasks = 50;

	private static String[] blockPhysicsWhitelist = new String[] {};

	private static String[] blockPhysicsBlacklist = new String[] {"minecraft:bedrock"};

	/**
	 * Retrieves a copy of the block physics whitelist in array form.
	 * @return array of whitelisted block ids
	 */
	public static String[] getBlockPhysicsWhitelist() {
		return Arrays.copyOf(blockPhysicsWhitelist, blockPhysicsWhitelist.length);
	}

	/**
	 * Retrieves a copy of the block physics blacklist in array form.
	 * @return array of blacklisted block ids
	 */
	public static String[] getBlockPhysicsBlacklist() {
		return Arrays.copyOf(blockPhysicsBlacklist, blockPhysicsBlacklist.length);
	}

	/**
	 * Loads the configuration file into memory for the mod to use.
	 */
	public static void loadConfig() {
		configFile.load();

		syncConfig();
	}
}
