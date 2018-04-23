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

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.event.BlockPhysicsEvent;
import tk.coaster3000.gravity.util.PhysicsUtil;

import java.util.Objects;
import javax.annotation.Nonnull;

public class BlockFellTask extends BlockTask implements Comparable<BlockTask>, ValidatedTask {
	private final IBlockState blockState;

	/**
	 * Constructs a block felling task that is used in the Physics scheduler to make blocks fall.  
	 * @param worldHandle involved in the task
	 * @param position within the world involved in the task 
	 */
	public BlockFellTask(IWorldHandle worldHandle, BlockPos position) {
		this(worldHandle, position, worldHandle.getBlockState(position).getActualState(worldHandle.getWorld(), position));
	}

	/**
	 * Constructs a block felling task that is used in the Physics scheduler to make blocks fall.
	 * @param worldHandle involved in the task
	 * @param position within the world involved in the task
	 * @param blockState expected state of block for task execution
	 */
	public BlockFellTask(IWorldHandle worldHandle, BlockPos position, IBlockState blockState) {
		super(worldHandle, position);
		this.blockState = blockState;
	}

	@Override
	public void run() {
		// This condition should not happen, but is possible.
		if (!isValid()) {
			markComplete();
			return;
		}

		IBlockState curState = worldHandle.getBlockState(position).getActualState(worldHandle.getWorld(), position);

		if (!MinecraftForge.EVENT_BUS.post(new BlockPhysicsEvent.Fall(worldHandle, position, curState)))
			PhysicsUtil.fellBlock(worldHandle, position, curState);

		//TODO: Cache the task for validation
		//TODO: Cache the result of the fell task to validate on event completion

		markComplete();
	}

	@Override
	public int compareTo(@Nonnull BlockTask o) {
		return this.position.compareTo(o.position);
	}

	@Override
	public boolean isValid() {
		IBlockState curState = worldHandle.getBlockState(position).getActualState(worldHandle.getWorld(), position);
		if (!Objects.equals(curState.getBlock().getRegistryName(), blockState.getBlock().getRegistryName())) return false;

		for (IProperty<?> property : blockState.getPropertyKeys()) {
			if (!curState.getValue(property).equals(blockState.getValue(property))) return false;
		}

		return true;
	}
}
