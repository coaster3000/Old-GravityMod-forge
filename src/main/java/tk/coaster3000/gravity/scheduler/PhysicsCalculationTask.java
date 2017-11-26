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
package tk.coaster3000.gravity.scheduler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tk.coaster3000.gravity.event.BlockPhysicsEvent;
import tk.coaster3000.gravity.util.PhysicsUtil;

public class PhysicsCalculationTask extends PhysicsTask {

	/**
	 * Constructs a calculation task that is used in the Physics scheduler to perform physics checks.
	 * @param scheduler used to schedule additional tasks once this task is complete
	 * @param world involved in the task
	 * @param position within the world involved in the task
	 */
	public PhysicsCalculationTask(PhysicsScheduler scheduler, World world, BlockPos position) {
		super(scheduler, world, position);
	}

	@Override
	void execute() {
		IBlockState blockState = world.getBlockState(position);
		BlockPhysicsEvent.Check bpcEvent = new BlockPhysicsEvent.Check(world, position, blockState);

		MinecraftForge.EVENT_BUS.post(bpcEvent);

		if (!bpcEvent.isCanceled()) {
			boolean isFalling = PhysicsUtil.canBlockFall(world, position, blockState);
			if (isFalling) {
				scheduler.scheduleTask(new PhysicsFellTask(scheduler, world, position));
			}
		}
	}

	@Override
	public int compareTo(PhysicsTask o) {
		return 0;
	}
}
