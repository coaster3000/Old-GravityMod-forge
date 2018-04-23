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
package tk.coaster3000.gravity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public interface IWorldHandle extends Supplier<World> {

	/**
	 * Retrieves the name of the world.
	 * @return name of the world as a String
	 */
	String getName();

	/**
	 * Retrieves the block state at the specified position within the world.
	 * @param position within the world
	 * @return a blockstate at the specified position within the world
	 */
	IBlockState getBlockState(BlockPos position);

	/**
	 * Retrieves the dimension id for the world.
	 * @return dimension id as an integer
	 */
	int getDimension();

	/**
	 * Retrieves the world involved in the handle.
	 * @return world
	 */
	World getWorld();

	/**
	 * Spawns the specified entity within the world.
	 * @param entity to spawn
	 */
	void spawnEntity(EntityFallingBlock entity);

	/**
	 * Checks if the specified position within the world is air.
	 * @param position to check
	 * @return true if air, false if not
	 */
	boolean isAirBlock(BlockPos position);

	@Override
	default World get() {
		return getWorld();
	}
}
