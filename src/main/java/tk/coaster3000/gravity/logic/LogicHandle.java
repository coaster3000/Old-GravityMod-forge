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

package tk.coaster3000.gravity.logic;

import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.BlockPos;
import tk.coaster3000.gravity.GravityMod;
import tk.coaster3000.gravity.IWorldHandle;

import java.util.Optional;

interface LogicHandle extends Comparable<LogicHandle> {
	/**
	 * Tells if this Logic Handle may run logic for the specified location within the world.
	 * @param handle of the world to check
	 * @param position within the world to check
	 * @return true if allowed to run logic, false if not
	 */
	boolean canHandle(IWorldHandle handle, BlockPos position);

	/**
	 * Handle the logic at the specified position within the specified world.
	 * @param handle of the world
	 * @param position within the world
	 * @return true if logic states to act, false otherwise
	 */
	Optional<LogicResult> run(IWorldHandle handle, BlockPos position);

	default boolean isClear(IWorldHandle handle, BlockPos position) {
		return (handle.isAirBlock(position) || BlockFalling.canFallThrough(handle.getBlockState(position)) || GravityMod.instance.getPhysicsScheduler().isDue(handle, position));
	}

	/**
	 * Retrieves the priority of this logic handle.
	 * @return priority of logic handle
	 */
	int getPriority();
}
