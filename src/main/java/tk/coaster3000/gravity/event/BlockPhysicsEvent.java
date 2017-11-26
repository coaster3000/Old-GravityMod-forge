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
package tk.coaster3000.gravity.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class BlockPhysicsEvent extends BlockEvent {
	BlockPhysicsEvent(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
	}

	/**
	 * Fired before and after physics checks are completed.
	 */
	public static class Check extends BlockPhysicsEvent {
		/**
		 * Constructs an event object from the specified world, position, and state parameters.
		 * @param world event involves
		 * @param position event involves
		 * @param state the block state involved at the position in the world
		 */
		public Check(World world, BlockPos position, IBlockState state) {
			super(world, position, state);
		}

		@Override
		public boolean isCancelable() {
			return true;
		}
	}

	/**
	 * Fired when physics is applied to a block and it begins to fall.
	 */
	public static class Fall extends BlockPhysicsEvent {
		/**
		 * Constructs an event object from the specified world, position, and state parameters.
		 * @param world event involves
		 * @param position event involves
		 * @param state the block state involved at the position in the world
		 */
		public Fall(World world, BlockPos position, IBlockState state) {
			super(world, position, state);
		}

		@Override
		public boolean isCancelable() {
			return true;
		}
	}
}
