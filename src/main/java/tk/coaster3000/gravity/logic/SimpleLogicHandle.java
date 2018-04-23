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

import net.minecraft.util.math.BlockPos;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.util.PhysicsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleLogicHandle extends BaseLogicHandle {

	@Override
	public boolean canHandle(IWorldHandle handle, BlockPos position) {
		return PhysicsUtil.isBlockAllowedPhysics(handle.getBlockState(position));
	}

	@Override
	public Optional<LogicResult> run(IWorldHandle handle, BlockPos position) {
		return Optional.ofNullable((canHandle(handle, position) && isClear(handle, position.down())) ? new LogicResult(handle, position, handle.getBlockState(position)) : null);
	}

	@Override
	public int getPriority() {
		return Integer.MAX_VALUE;
	}
}
