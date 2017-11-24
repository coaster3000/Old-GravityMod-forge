package tk.coaster3000.gravity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PhysicsScheduler {

	private List<String> worldIDList;
	private List<Queue<PhysicsCalculationTask>> calcTaskList;
	private List<Queue<PhysicsFellTask>> fellTaskList;


	private static final int CALC_INDEX = 0, FALL_INDEX = 1;

	//TODO: Implement configuration settings
	private int calculationLimit = 100; // Max physics tasks that involve calculations.
	private int fellLimit = 50; // Max physics tasks that involve blocks falling.

	public PhysicsScheduler() {
		worldIDList = new ArrayList<>();

		calcTaskList = new ArrayList<>();
		fellTaskList = new ArrayList<>();
	}

	/**
	 * Used to add a world to the tick handler.
	 * @param world to add
	 */
	void addWorld(World world) {
		String id;
		if (worldIDList.contains(id = getWorldKey(world))) return;

		worldIDList.add(id);
		calcTaskList.add(new PriorityQueue<>());
		fellTaskList.add(new PriorityQueue<>());
	}

	/**
	 * Used to remove the tick handler from a world.
	 * Normally is called when a world is unloaded.
	 * @param world to remove
	 */
	void removeWorld(World world) {
		String id;
		if (worldIDList.contains(id = getWorldKey(world))) {
			//TODO: Implement physics check serialization to store uncalculated physics.
			int index = worldIDList.indexOf(id);
			calcTaskList.remove(index);
			fellTaskList.remove(index);
			worldIDList.remove(index);
		}
	}

	/**
	 * Tells if the world is processed by the scheduler.
	 * @param world to check
	 * @return true if world gets processed in scheduler, false if not
	 */
	private boolean hasWorld(World world) {
		return hasWorld(getWorldKey(world));
	}

	/**
	 * Tells if a world is processed by the scheduler based on the ID provided.
	 * <p>ID format is <pre>name:dimID</pre></p>
	 * @param worldKey world id to check
	 * @return true if world is processed in here, otherwise false
	 */
	private boolean hasWorld(String worldKey) {
		return worldIDList.contains(worldKey);
	}

	/**
	 * Tells if the world has work to be processed.
	 * <p>if the world does not exist in the scheduler, will always return false.</p>
	 * @param world to check
	 * @return true if work present on world, otherwise false
	 */
	boolean hasWork(World world) {
		return hasWork(getWorldKey(world));
	}

	/**
	 * Tells if the world has work to be processed based on the ID provided.
	 * <p>ID format is <pre>name:dimID</pre></p>
	 * @param worldKey world id to check
	 * @return true if work present, otherwise false
	 */
	private boolean hasWork(String worldKey) {
		int i = worldIDList.indexOf(worldKey); // index of world
		return hasWorld(worldKey) && (!calcTaskList.get(i).isEmpty() || !fellTaskList.get(i).isEmpty());
	}

	/**
	 * Called for a world tick to handle physics tasks for specified world.
	 * @param world to handle physics
	 */
	void handleTick(World world) {
		String id;
		if (hasWork(id = getWorldKey(world))) {
			List<PhysicsTask> tasks = new ArrayList<>();

			Queue<PhysicsCalculationTask> calcTasks = calcTaskList.get(worldIDList.indexOf(id));
			Queue<PhysicsFellTask> fellTasks = fellTaskList.get(worldIDList.indexOf(id));

			int ft = 0, ct = 0; // Fell Tasks, Calculation Tasks
			boolean mft = fellTasks.isEmpty(), mct = calcTasks.isEmpty(); // Max Fell Tasks Met, Max Calculation Tasks Met

			while (!mft || !mct) {
				if (calculationLimit < ct++ || calcTasks.isEmpty()) mct = true;
				if (fellLimit < ft++ || fellTasks.isEmpty()) mft = true;

				if (!mft) tasks.add(fellTasks.remove());
				if (!mct) tasks.add(calcTasks.remove());
			}

			tasks.forEach(PhysicsTask::execute);
		}

//		if (worldIDList.contains(id = getWorldKey(world)) && !(tasks = calcTaskList.get(worldIDList.indexOf(id))).isEmpty()) {
//			int i = 0;
//			while (calculationLimit > i++ && !tasks.isEmpty()) {
//				PhysicsTask task = tasks.remove(0);
//				task.execute();
//			}
//		}
	}


	/**
	 * @throws IllegalArgumentException when task is neither a PhysicsCalculationTask or PhysicsFellTask
	 * @param task to schedule
	 */
	void scheduleTask(PhysicsTask task) {
		String id;
		if (hasWorld(id = getWorldKey(task.world)))
			if (task instanceof PhysicsCalculationTask)
				calcTaskList.get(worldIDList.indexOf(id)).add((PhysicsCalculationTask) task);
			else if (task instanceof PhysicsFellTask)
				fellTaskList.get(worldIDList.indexOf(id)).add((PhysicsFellTask) task);
			else
				throw new IllegalArgumentException("Invalid task supplied to PhysicsScheduler!");
	}

	@Deprecated
	public void scheduleCalcTask(World world, BlockPos position) {
		scheduleTask(new PhysicsCalculationTask(this, world, position));
	}

	/**
	 * Retrieves the maximum allowed tasks per tick.
	 * @return
	 */
	public int getCalculationLimit() {
		return calculationLimit;
	}

	/**
	 * Sets the maximum allowed tasks per tick.
	 * @param calculationLimit to set
	 */
	public void setCalculationLimit(int calculationLimit) {
		this.calculationLimit = calculationLimit;
	}

	private static String getWorldKey(World world) {
		return world.getWorldInfo().getWorldName() + ":" + world.provider.getDimension();
	}
}
