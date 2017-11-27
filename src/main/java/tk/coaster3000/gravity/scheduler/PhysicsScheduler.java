/**
 * Copyright 2017 Coaster3000 (Christopher Krier)
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
package tk.coaster3000.gravity.scheduler;

import net.minecraft.util.math.BlockPos;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.common.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class PhysicsScheduler {

	private List<String> worldIdList;
	private List<Queue<PhysicsCalculationTask>> calcTaskList;
	private List<Queue<PhysicsFellTask>> fellTaskList;


	private static final int CALC_INDEX = 0;
	private static final int FALL_INDEX = 1;

	/**
	 * Constructs a new PhysicsScheduler object to handle physics tasks.
	 */
	public PhysicsScheduler() {
		worldIdList = new ArrayList<>();

		calcTaskList = new ArrayList<>();
		fellTaskList = new ArrayList<>();
	}

	/**
	 * Used to add a world to the tick handler.
	 * @param world to add
	 */
	public void addWorld(IWorldHandle world) {
		String id;
		if (worldIdList.contains(id = getWorldKey(world))) return;

		worldIdList.add(id);
		calcTaskList.add(new PriorityQueue<>());
		fellTaskList.add(new PriorityQueue<>());
	}

	/**
	 * Used to remove the tick handler from a world.
	 * Normally is called when a world is unloaded.
	 * @param world to remove
	 */
	public void removeWorld(IWorldHandle world) {
		String id;
		if (worldIdList.contains(id = getWorldKey(world))) {
			//TODO: Implement physics check serialization to store uncalculated physics.
			int index = worldIdList.indexOf(id);
			calcTaskList.remove(index);
			fellTaskList.remove(index);
			worldIdList.remove(index);
		}
	}

	/**
	 * Tells if the world is processed by the scheduler.
	 * @param world to check
	 * @return true if world gets processed in scheduler, false if not
	 */
	private boolean hasWorld(IWorldHandle world) {
		return hasWorld(getWorldKey(world));
	}

	/**
	 * Tells if a world is processed by the scheduler based on the ID provided.
	 * <p>ID format is <pre>name:dimID</pre></p>
	 * @param worldKey world id to check
	 * @return true if world is processed in here, otherwise false
	 */
	private boolean hasWorld(String worldKey) {
		return worldIdList.contains(worldKey);
	}

	/**
	 * Tells if the world has work to be processed.
	 * <p>if the world does not exist in the scheduler, will always return false.</p>
	 * @param world to check
	 * @return true if work present on world, otherwise false
	 */
	public boolean hasWork(IWorldHandle world) {
		return hasWork(getWorldKey(world));
	}

	/**
	 * Tells if the world has work to be processed based on the ID provided.
	 * <p>ID format is <pre>name:dimID</pre></p>
	 * @param worldKey world id to check
	 * @return true if work present, otherwise false
	 */
	private boolean hasWork(String worldKey) {
		int i = worldIdList.indexOf(worldKey); // index of world
		return hasWorld(worldKey) && (!calcTaskList.get(i).isEmpty() || !fellTaskList.get(i).isEmpty());
	}

	/**
	 * Called for a world tick to handle physics tasks for specified world.
	 * @param world to handle physics
	 */
	public void handleTick(IWorldHandle world) {
		String id;
		if (hasWork(id = getWorldKey(world))) {
			List<PhysicsTask> tasks = new ArrayList<>();

			Queue<PhysicsCalculationTask> calcTasks = calcTaskList.get(worldIdList.indexOf(id));
			Queue<PhysicsFellTask> fellTasks = fellTaskList.get(worldIdList.indexOf(id));

			int ft = 0; // Fell Tasks
			int ct = 0; // Calculation Tasks
			boolean mft = fellTasks.isEmpty(); // Max Fell Tasks Met
			boolean mct = calcTasks.isEmpty(); // Max Calculation Tasks Met

			while (!mft || !mct) {
				if (Config.maxPhysicsCalculationTasks < ct++ || calcTasks.isEmpty()) mct = true;
				if (Config.maxPhysicsFellTasks < ft++ || fellTasks.isEmpty()) mft = true;

				if (!mft) tasks.add(fellTasks.remove());
				if (!mct) tasks.add(calcTasks.remove());
			}

			tasks.forEach(PhysicsTask::execute);
		}

//		if (worldIdList.contains(id = getWorldKey(world)) && !(tasks = calcTaskList.get(worldIdList.indexOf(id))).isEmpty()) {
//			int i = 0;
//			while (calculationLimit > i++ && !tasks.isEmpty()) {
//				PhysicsTask task = tasks.remove(0);
//				task.execute();
//			}
//		}
	}


	/**
	 * Schedules a task into the scheduler to be performed during tick handling.
	 * @param task to schedule
	 * @throws IllegalArgumentException when task is neither a PhysicsCalculationTask or PhysicsFellTask
	 */
	void scheduleTask(PhysicsTask task) {
		String id;
		if (hasWorld(id = getWorldKey(task.worldHandle)))
			if (task instanceof PhysicsCalculationTask)
				calcTaskList.get(worldIdList.indexOf(id)).add((PhysicsCalculationTask) task);
			else if (task instanceof PhysicsFellTask)
				fellTaskList.get(worldIdList.indexOf(id)).add((PhysicsFellTask) task);
			else
				throw new IllegalArgumentException("Invalid task supplied to PhysicsScheduler!");
	}

	/**
	 * Creates and schedules a calculation task within the specified world, at the specified location.
	 * @param world involved in the task
	 * @param position within the world involving this task
	 * @deprecated use {@link #scheduleTask(PhysicsTask)} instead
	 */
	@Deprecated
	public void scheduleCalcTask(IWorldHandle world, BlockPos position) {
		scheduleTask(new PhysicsCalculationTask(this, world, position));
	}

	private static String getWorldKey(IWorldHandle world) {
		return world.getName() + ":" + world.getDimension();
	}
}
