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

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import tk.coaster3000.gravity.GravityMod;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.event.BlockPhysicsEvent;
import tk.coaster3000.gravity.logic.LogicResult;

import java.util.NoSuchElementException;
import java.util.Optional;

public class LogicTask extends BlockTask implements Comparable<BlockTask>, FutureValue<Optional<LogicResult>> {
	private final transient PhysicsScheduler physicsScheduler;
	private final transient  LogicScheduler logicScheduler;
	private transient LogicResult result = null;

	/**
	 * Constructs a calculation task that is used in the Physics physicsScheduler to perform physics checks.
	 * @param worldHandle involved in the task
	 * @param position within the world involved in the task
	 */
	LogicTask(IWorldHandle worldHandle, BlockPos position) {
		super(worldHandle, position);
		this.physicsScheduler = GravityMod.instance.getPhysicsScheduler();
		this.logicScheduler = GravityMod.instance.getLogicScheduler();
	}

	@Override
	public void run() {

		if (!MinecraftForge.EVENT_BUS.post(new BlockPhysicsEvent.Check(worldHandle, position, worldHandle.getBlockState(position)))) {
			Optional<LogicResult> block = GravityMod.instance.getLogicController().runCheck(worldHandle, position);
			result = block.orElse(null);

			block.ifPresent(logicResults -> {
				physicsScheduler.addTask(result.handle, result.position, result.blockState);
				for (EnumFacing face : EnumFacing.VALUES) {
					if (!physicsScheduler.isDue(result.handle, result.position.offset(face)))
						logicScheduler.addTask(result.handle, result.position.offset(face));
				}
			});

		}
		markComplete();
	}

	@Override
	public Optional<LogicResult> getValue() throws NoSuchElementException {
		if (!hasValue()) throw new NoSuchElementException("Task has not been executed yet!");
		return Optional.ofNullable(result);
	}

	@Override
	public boolean hasValue() {
		return isCompleted();
	}

	@Override
	public int compareTo(BlockTask o) {
		return this.position.compareTo(o.position);
	}
}
