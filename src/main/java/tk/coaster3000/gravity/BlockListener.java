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

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.coaster3000.gravity.scheduler.LogicScheduler;
import tk.coaster3000.gravity.util.Util;

public class BlockListener {

	private final LogicScheduler logicScheduler;

	BlockListener(LogicScheduler scheduler) {
		this.logicScheduler = scheduler;
	}

	/**
	 * Fired when block placements are done.
	 * @param event involved
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onBlockPlace(BlockEvent.PlaceEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return; //Ignore the event
		BlockPos pos = event.getPos();

		logicScheduler.addTask(Util.wrapWorld(world), pos);
	}

	/**
	 * Fired when neighbor change events occur (Block Updates).
	 * @param event involved
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onBlockNeighborChange(BlockEvent.NeighborNotifyEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		BlockPos sPos = event.getPos();
		for (EnumFacing face : event.getNotifiedSides()) { // Test horizontal
			BlockPos pos = sPos.offset(face);

			logicScheduler.addTask(Util.wrapWorld(world), pos);
		}


	}
}
