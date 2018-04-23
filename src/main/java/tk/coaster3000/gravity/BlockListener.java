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

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.coaster3000.gravity.scheduler.PhysicsScheduler;

public class BlockListener {

	/**
	 * Constructs the BlockListener object that listens to block events.
	 * @param scheduler to feed events to.
	 */
	public BlockListener(PhysicsScheduler scheduler) {
		this.scheduler = scheduler;
	}

	PhysicsScheduler scheduler;

	private boolean canFall(IWorldHandle worldHandle, BlockPos pos) {
		return canFall(worldHandle, pos, worldHandle.getBlockState(pos));
	}

	private boolean canFall(IWorldHandle worldHandle, BlockPos pos, IBlockState blockState) {
		return !(blockState.getBlock().getRegistryName() != null && blockState.getBlock().getRegistryName().toString().equals("minecraft:bedrock")) && (worldHandle.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldHandle.getBlockState(pos.down())));
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

		scheduler.scheduleCalcTask(new WorldHandle(world), pos);
	}

	/**
	 * Fired when neighbor change events occur (Block Updates).
	 * @param event involved
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onBlockNeighborChange(BlockEvent.NeighborNotifyEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		BlockPos sPos = event.getPos(); // FIXME: Blocks near final layer of world will cause world to PLUMMET INTO DARKNESS
		for (EnumFacing face : event.getNotifiedSides()) { // Test horizontal
			BlockPos pos = sPos.offset(face);
			scheduler.scheduleCalcTask(new WorldHandle(world), pos);

		}

	}
}
