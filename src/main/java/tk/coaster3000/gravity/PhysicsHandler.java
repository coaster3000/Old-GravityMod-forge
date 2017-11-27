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
package tk.coaster3000.gravity;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tk.coaster3000.gravity.event.PhysicsTickEvent;
import tk.coaster3000.gravity.event.PhysicsTickEvent.Phase;
import tk.coaster3000.gravity.scheduler.PhysicsScheduler;

public class PhysicsHandler {

	private PhysicsScheduler physicsScheduler;

	/**
	 * Constructs a PhysicsHandler which handles game events to produce physics tasks.
	 * @param physicsScheduler scheduler used to add physics tasks.
	 */
	public PhysicsHandler(PhysicsScheduler physicsScheduler) {
		this.physicsScheduler = physicsScheduler;
	}

	void onPhysicsTick(IWorldHandle worldHandle) {
		physicsScheduler.handleTick(worldHandle);
	}

	/**
	 * Called during world tick events to handle physics tasks within the scheduler.
	 * @param event involved in the world tick
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onEvent(TickEvent.WorldTickEvent event) {
		IWorldHandle worldHandle = new WorldHandle(event.world);
		if (!physicsScheduler.hasWork(worldHandle)) return;

		PhysicsTickEvent tickEvent = new PhysicsTickEvent(Phase.START, worldHandle);
		MinecraftForge.EVENT_BUS.post(tickEvent);

		if (!tickEvent.isCanceled()) {
			onPhysicsTick(tickEvent.worldHandle);
			tickEvent = new PhysicsTickEvent(Phase.END, worldHandle);
			MinecraftForge.EVENT_BUS.post(tickEvent);
		}
	}
}
