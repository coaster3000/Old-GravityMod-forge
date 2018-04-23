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

package tk.coaster3000.gravity.scheduler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.gravity.GravityMod;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.common.Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class PhysicsScheduler implements Scheduler<BlockFellTask> {
	private static SortedMap<BlockPos, BlockFellTask> EMPTY_TASKS = Collections.unmodifiableSortedMap(new TreeMap<>());

	private final Map<String, SortedMap<BlockPos, BlockFellTask>> tasks;
	private volatile boolean hasWork = false;

	private final Logger logger;

	/**
	 * Constructs a PhysicsScheduler Object.
	 */
	public PhysicsScheduler() {
		this.tasks = Collections.synchronizedMap(new HashMap<>());
		this.logger = GravityMod.instance.getLogger();
	}

	/**
	 * Tell's if physics at the specified location within the specified world is due.
	 * @param worldKey of the specified world
	 * @param position within the world
	 * @return true if block has physics due, false otherwise
	 */
	public boolean isDue(String worldKey, BlockPos position) {
		final boolean ret;

		synchronized (tasks) {
			ret = tasks.getOrDefault(worldKey, EMPTY_TASKS).containsKey(position);
		}

		return ret;
	}

	/**
	 * Tell's if physics at the specified location within the specified world is due.
	 * @param worldHandle of the world
	 * @param position within the world
	 * @return true if block has physics due, false otherwise
	 */
	public boolean isDue(IWorldHandle worldHandle, BlockPos position) {
		return isDue(getKey(worldHandle), position);
	}

	/**
	 * Adds a new physics task at the designated position within the designated world.
	 * @param worldHandle of the world
	 * @param position within the world
	 */
	public void addTask(IWorldHandle worldHandle, BlockPos position) {
		addTask(new BlockFellTask(worldHandle, position));
	}

	/**
	 * Adds a new physics task at the designated position within the designated world which runs only if the block state matches the designated block state.
	 * @param worldHandle of the world
	 * @param position within the world
	 * @param blockState to match
	 */
	public void addTask(IWorldHandle worldHandle, BlockPos position, IBlockState blockState) {
		addTask(new BlockFellTask(worldHandle, position, blockState));
	}

	@Override
	public void addTask(BlockFellTask task) {
		final String worldKey = getKey(task.worldHandle);

		synchronized (tasks) {
			if (!tasks.containsKey(worldKey)) {
				this.logger.error("Fell task issued on an non-tracked world! The world: '" + worldKey + "' is not tracked!");
			} else {
				final SortedMap<BlockPos, BlockFellTask> work;
				work = tasks.get(worldKey);
				synchronized (work) { //TODO: Test for deadlock scenario
					work.put(task.position, task);
				}
				hasWork = true;
			}
		}
	}

	/**
	 * Tells whether or not the scheduler manages the specified world.
	 * @param handle of the world
	 * @return true if the scheduler manages the specified world, false otherwise
	 */
	public boolean hasWorld(IWorldHandle handle) {
		return hasWorld(getKey(handle));
	}

	/**
	 * Tells whether or not the scheduler manages the specified world.
	 * @param worldKey of the world
	 * @return true if the scheduler manages the specified world, false otherwise
	 */
	public boolean hasWorld(String worldKey) {
		return tasks.containsKey(worldKey);
	}

	/**
	 * Adds a world to the queue system.
	 * @param handle of the world to add
	 */
	public void addWorld(IWorldHandle handle) {
		final String worldKey = getKey(handle);
		synchronized (tasks) {
			tasks.putIfAbsent(worldKey, Collections.synchronizedSortedMap(new TreeMap<>()));
		}
	}

	/**
	 * Removes a world from the queue system.
	 * @param handle of the world to remove
	 */
	public void removeWorld(IWorldHandle handle) {
		final String worldKey = getKey(handle);
		synchronized (tasks) {
			if (hasWork(worldKey)) logger.info("Unfinished work in world '" + handle.getName() + "'!");
			tasks.remove(worldKey);
		}
	}

	@Override
	public boolean hasTasks() {
		return hasWork; //TODO: Check thread safety
	}

	/**
	 * Executes queued tasks.
	 * <p>Implementation makes use of a work limit which stops task execution after a certain point.
	 * Subsequent calls to this method will continue executing the tasks within the queue.</p>
	 * @param worldHandle of the world the tasks are running
	 */
	public void run(IWorldHandle worldHandle) {
		if (!hasWork || !hasWork(worldHandle)) return;

		final SortedMap<BlockPos, BlockFellTask> work;
		work = tasks.getOrDefault(getKey(worldHandle), EMPTY_TASKS);
		synchronized (work) {
			int max = Config.maxPhysicsFellTasks;
			if (work.size() > max) {
				Iterator<BlockPos> it = work.keySet().iterator();
				int i = 0;
				while (i++ < max) {
					if (!it.hasNext()) {
						checkWork();
						break; //No more work
					}

					work.get(it.next()).run(); // Run Task
					it.remove(); //Completed Task
				}
			} else {
				work.values().forEach(BlockFellTask::run);
				work.clear();
			}
		}
		checkWork();
	}

	private void checkWork() {
		synchronized (tasks) {
			hasWork = false;
			tasks.forEach((key, map) -> {
				synchronized (map) {
					hasWork = hasWork || !map.isEmpty();
				}
			});
		}
	}

	/**
	 * Tells if there is any work to be done within the specified world.
	 * @param worldKey of the world
	 * @return true if work is present, false otherwise
	 */
	public boolean hasWork(String worldKey) {
		return !tasks.getOrDefault(worldKey, EMPTY_TASKS).isEmpty();
	}

	/**
	 * Tells if there is any work to be done within the specified world.
	 * @param worldHandle of world
	 * @return true if work is present, false otherwise
	 */
	public boolean hasWork(IWorldHandle worldHandle) {
		return hasWork(getKey(worldHandle));
	}

	private String getKey(IWorldHandle handle) {
		return handle.getName() + ":" + handle.getDimension();
	}
}
