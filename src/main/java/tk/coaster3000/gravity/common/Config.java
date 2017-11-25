package tk.coaster3000.gravity.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Config {
	private Config() {}


	static Configuration configFile;

	private final static String TOGGLES = "Enable-Disable";

	private final static String LIMITS = "Limits";

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

	static {
		String key = "maxPhysicsCalculationTasks";
		configRetrieval.put(key, () -> Config.maxPhysicsCalculationTasks);
		configSetters.put(key, (Integer i) -> Config.maxPhysicsCalculationTasks = i);
		configTypes.put(key, Integer.class);

		key = "maxPhysicsFellTasks";
		configRetrieval.put(key, () -> Config.maxPhysicsFellTasks);
		configSetters.put(key, (Integer i) -> Config.maxPhysicsFellTasks = i);
		configTypes.put(key, Integer.class);
	}

	public static void load(FMLPreInitializationEvent event) {
		configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.1", false);
		configFile.load();

		syncConfig();
	}

	public static boolean saveConfig() {
		configFile.get(LIMITS, maxCalculationTasksPerTickKey, maxPhysicsCalculationTasks, "Determines how many physics calculation tasks are allowed to execute per world per tick.", 1, Integer.MAX_VALUE).set(maxPhysicsCalculationTasks);
		configFile.get(LIMITS, maxFellTasksPerTickKey, maxPhysicsFellTasks, "Determines how many physics block falling tasks can execute per world per tick.", 1, Integer.MAX_VALUE).set(maxPhysicsFellTasks);

		boolean changed = false;
		if (configFile.hasChanged()) {
			configFile.save();
			changed = true;
		}

		return changed;
	}

	public static boolean syncConfig() {
		maxPhysicsCalculationTasks = configFile.get(LIMITS, maxCalculationTasksPerTickKey, maxPhysicsCalculationTasks, "Determines how many physics calculation tasks are allowed to execute per world per tick.", 1, Integer.MAX_VALUE).getInt(maxPhysicsCalculationTasks);
		maxPhysicsFellTasks = configFile.get(LIMITS, maxFellTasksPerTickKey, maxPhysicsFellTasks, "Determines how many physics block falling tasks can execute per world per tick.", 1, Integer.MAX_VALUE).getInt(maxPhysicsFellTasks);


		boolean changed = false;
		if (configFile.hasChanged()) {
			configFile.save();
			changed = true;
		}

		return changed;
	}
	public static int maxPhysicsCalculationTasks = 100;

	public static int maxPhysicsFellTasks = 50;

	public static void loadConfig() {
		configFile.load();

		syncConfig();
	}
}
