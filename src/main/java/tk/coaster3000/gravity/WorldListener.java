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

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.coaster3000.gravity.scheduler.PhysicsScheduler;
import tk.coaster3000.gravity.util.Util;

public class WorldListener {

	private final PhysicsScheduler scheduler;

	WorldListener(PhysicsScheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Fired when the World Load event is called.
	 * @param event involved during world load
	 */
	@SubscribeEvent
	public void onEvent(WorldEvent.Load event) {
		if (event.getWorld().isRemote) return;

		scheduler.addWorld(Util.wrapWorld(event.getWorld()));
	}

	/**
	 * Fired when the World Unload event is called.
	 * @param event involved during world unload
	 */
	@SubscribeEvent
	public void onEvent(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) return;

		scheduler.removeWorld(Util.wrapWorld(event.getWorld()));
	}
}
