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

import java.util.Objects;


abstract class BlockTask implements Task {

	final IWorldHandle worldHandle;
	final BlockPos position;
	private boolean isComplete;

	BlockTask(IWorldHandle worldHandle, BlockPos position) {
		this.worldHandle = worldHandle;
		this.position = position;
	}

	/**
	 * Marks the task as complete.
	 */
	final void markComplete() {
		this.isComplete = true;
	}

	@Override
	public final boolean isCompleted() {
		return this.isComplete;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		BlockTask blockTask = (BlockTask) o;
		return Objects.equals(worldHandle, blockTask.worldHandle) && Objects.equals(position, blockTask.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(worldHandle.getDimension(), position);
	}
}
