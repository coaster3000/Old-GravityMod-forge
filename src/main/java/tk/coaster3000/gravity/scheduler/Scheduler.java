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

public interface Scheduler<T extends Task> {

	/**
	 * Adds a task to complete to the scheduler.
	 * @param task to run
	 */
	 void addTask(T task);

	/**
	 * Tells whether there is any tasks that is scheduled
	 * @return true if tasks exist, false otherwise
	 */
	boolean hasTasks();

	/**
	 * Tells whether the scheduler is shut down.
	 * @return true if shutdown, false if not
	 */
	default boolean isShutdown() {
		return false;
	}

	/**
	 * Shuts down the scheduler, preventing the ability to add additional tasks to the queue.
	 */
	default void shutdown() { }

}
