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
import net.minecraftforge.common.MinecraftForge;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.event.GravityLogicLoadEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LogicController {

	private List<LogicHandle> logicHandles = new ArrayList<>();

	private boolean dirty = false;

	/**
	 * Adds a new LogicHandle to the controller to use.
	 * @param handle to add
	 */
	public void addLogicHandle(LogicHandle handle) {
		logicHandles.add(handle);
		dirty = true;
	}

	/**
	 * Runs physics logic on the specified location within the specified world.
	 * @param worldHandle of the world to run
	 * @param position within the world to run logic
	 * @return true if the block at the specified location should fall, false otherwise
	 */
	public Optional<LogicResult> runCheck(IWorldHandle worldHandle, BlockPos position) {

		for (LogicHandle handle : getHandles()) {
			if (handle.canHandle(worldHandle, position)) return handle.run(worldHandle, position);
		}
		return Optional.empty();
	}

	private void clean() {
		Collections.sort(logicHandles);
		dirty = false;
	}

	/**
	 * Reloads the logic controller.
	 * <p><b>This should not be called from an outside mod! Be Respectful!</b></p>
	 */
	public final void reload() {
		logicHandles.clear();
		preloadLogic();
		GravityLogicLoadEvent loadEvent = new GravityLogicLoadEvent(this);
		MinecraftForge.EVENT_BUS.post(loadEvent);
	}


	private void preloadLogic() {
		addLogicHandle(new SimpleLogicHandle());
	}

	List<LogicHandle> getHandles() {
		if (dirty) clean();
		return Collections.unmodifiableList(logicHandles);
	}
}
