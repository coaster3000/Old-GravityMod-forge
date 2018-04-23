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

import net.minecraft.util.math.BlockPos;
import tk.coaster3000.gravity.IWorldHandle;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogicScheduler implements Scheduler<LogicTask>, Runnable {
	private final Object taskLock = new Object();
	private final ArrayList<LogicTask> tasks;
	private ExecutorService executor;


	/**
	 * Constructs a LogicScheduler object.
	 */
	public LogicScheduler() {
		this.tasks = new ArrayList<>();
		executor = Executors.newCachedThreadPool();
	}

	/**
	 * Schedules a logic check at the specified position within the specified world.
	 * @param worldHandle of the specified world
	 * @param position within the specified world
	 */
	public void schedule(IWorldHandle worldHandle, BlockPos position) {
		synchronized (taskLock) {
			addTask(new LogicTask(worldHandle, position));
		}
		executor.execute(this);
	}


	/**
	 * Helper method to add a LogicTask.
	 * see {@link #addTask(LogicTask)}
	 * @param worldHandle of the world where the task is to take place
	 * @param position within the specified world for the task.
	 */
	public void addTask(IWorldHandle worldHandle, BlockPos position) {
		addTask(new LogicTask(worldHandle, position));
	}

	@Override
	public void addTask(LogicTask task) {
		if (task.isCompleted()) return;
		synchronized (taskLock) {
			tasks.add(task);
		}
		executor.submit(this);
	}

	@Override
	public boolean hasTasks() {
		return !this.tasks.isEmpty();
	}

	@Override
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	@Override
	public void shutdown() {
		executor.shutdown();
	}

	@Override
	public void run() {
		if (!hasTasks()) return;
		LogicTask task;
		synchronized (taskLock) {
			task = tasks.remove(0);
		}
		task.run();
		if (!task.isCompleted()) {
			synchronized (taskLock) {
				tasks.add(task);
			}
		}
	}
}
