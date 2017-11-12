package tk.coaster3000.gravity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.coaster3000.gravity.event.BlockPhysicsEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhysicsScheduler {

	private List<String> worldIDList;
	private List<List<PhysicsTask>> taskList;

	//TODO: Implement configuration settings
	private int physicsLimit = 100; // Max physics tasks per handleTick call

	public PhysicsScheduler() {
		worldIDList = new ArrayList<>();
		taskList = new ArrayList<>();
	}

	/**
	 * Used to add a world to the tick handler.
	 * @param world to add
	 */
	void addWorld(World world) {
		String id;
		if (worldIDList.contains(id = getWorldKey(world))) return;

		worldIDList.add(id);
		taskList.add(new ArrayList<>());
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
			taskList.remove(index);
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
	 * Tells if the world has work to be processed
	 * @param world to check
	 * @return true if work present, otherwise false
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
		return hasWorld(worldKey) && !taskList.get(worldIDList.indexOf(worldKey)).isEmpty();
	}

	/**
	 * Called for a world tick to handle physics tasks for specified world.
	 * @param world to handle physics
	 */
	void handleTick(World world) {
		physicsLimit = 100;
		String id;
		List<PhysicsTask> tasks;
		if (worldIDList.contains(id = getWorldKey(world)) && !(tasks = taskList.get(worldIDList.indexOf(id))).isEmpty()) {
			int i = 0;
			while (physicsLimit > i++ && !tasks.isEmpty()) {
				PhysicsTask task = tasks.remove(0);
				task.execute();
			}
		}
	}

	/**
	 * Retrieves a read-only view of the task list.
	 * @param world to grab list from
	 * @return unmodifiable view of the PhysicsTask List
	 */
	List<PhysicsTask> getTasks(World world) {
		return getTasks(getWorldKey(world));
	}

	/**
	 * Retrieves a read-only view of the task list.
	 * @param id to grab list from
	 * @return unmodifiable view of the PhysicsTask List
	 */
	List<PhysicsTask> getTasks(String id) {
		if (!hasWorld(id)) return null;
		return Collections.unmodifiableList(taskList.get(worldIDList.indexOf(id)));
	}

	public void scheduleTask(World world, BlockPos position) {
		if (hasWorld(world))
			taskList.get(worldIDList.indexOf(getWorldKey(world))).add(new PhysicsTask(world, position));
	}

	/**
	 * Retrieves the maximum allowed tasks per tick.
	 * @return
	 */
	public int getPhysicsLimit() {
		return physicsLimit;
	}

	/**
	 * Sets the maximum allowed tasks per tick.
	 * @param physicsLimit to set
	 */
	public void setPhysicsLimit(int physicsLimit) {
		this.physicsLimit = physicsLimit;
	}

	private static String getWorldKey(World world) {
		return world.getWorldInfo().getWorldName() + ":" + world.provider.getDimension();
	}
}
