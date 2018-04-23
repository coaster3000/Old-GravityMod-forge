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

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import tk.coaster3000.gravity.IWorldHandle;

public class LogicResult {
	public final IWorldHandle handle;
	public final BlockPos position;
	public final IBlockState blockState;

	/**
	 * Constructs a Logic Result Object containing the world, position within the world, and expected block state that was used during the test for the specified result.
	 * @param handle of the world
	 * @param position position within the world
	 * @param blockState used during test for this result
	 */
	public LogicResult(IWorldHandle handle, BlockPos position, IBlockState blockState) {
		this.handle = handle;
		this.position = position;
		this.blockState = blockState;
	}
}
