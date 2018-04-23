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

public class WorldHandle implements IWorldHandle {
	private final World world;

	/**
	 * Creates a world handle using the specified world.
	 * @param world for the handle to interact with
	 */
	public WorldHandle(World world) {
		this.world = world;
	}


	@Override
	public String getName() {
		return world.getWorldInfo().getWorldName();
	}

	@Override
	public IBlockState getBlockState(BlockPos position) {
		return world.getBlockState(position);
	}

	@Override
	public int getDimension() {
		return world.provider.getDimension();
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void spawnEntity(EntityFallingBlock entity) {
		world.spawnEntity(entity);
	}

	@Override
	public boolean isAirBlock(BlockPos position) {
		return world.isAirBlock(position);
	}
}
