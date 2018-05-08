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
package tk.coaster3000.gravity.util;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.common.Config;

public class PhysicsUtil {

	private static boolean isStringEqual(ResourceLocation resource, String string) {
		return string.equals((resource != null ? resource.toString() : ""));
	}


	/**
	 * Tells whether a block is allowed to fall.
	 * @param blockState block type
	 * @return true if block is not air or bedrock and is allowed to fall, false otherwise
	 */
	public static boolean isBlockAllowedPhysics(IBlockState blockState) {
		if (blockState.getBlock() == Blocks.AIR || BlockFalling.canFallThrough(blockState)) return false; // Ever kill the world with air? Ya, let's not

		String[] bla = Config.getBlockPhysicsBlacklist(); //BlackListArray
		String[] wla = Config.getBlockPhysicsWhitelist(); //WhiteListArray

		boolean ret = true;
		for (String bl : bla) {
			if (isStringEqual(blockState.getBlock().getRegistryName(),bl)) {
				ret = false;
				break;
			}
		}

		for (String wl : wla) {
			if (isStringEqual(blockState.getBlock().getRegistryName(), wl)) {
				ret = true;
				break;
			}
		}

		return ret;
	}

	/**
	 * Turns a block in specified world at specified position to fall using the current block state at time of call.
	 * @param world to have the block fall in
	 * @param position within the world to have the block fall in
	 */
	public static void fellBlock(IWorldHandle world, BlockPos position) {
		fellBlock(world, position, world.getBlockState(position));
	}

	/**
	 * Turns a block in specified world at specified position to fall with the specified blockState.
	 * @param world to have the block fall in
	 * @param position within the world to have the block fall in
	 * @param blockState block state to use when having the block fall within the world
	 */
	public static void fellBlock(IWorldHandle world, BlockPos position, IBlockState blockState) {
		double x = position.getX() + 0.5;
		double y = position.getY();
		double z = position.getZ() + 0.5;

		EntityFallingBlock ent = new EntityFallingBlock(world.getWorld(), x, y, z, blockState);
		ent.setOrigin(position.add(0.5,0,0.5));
		ent.preventEntitySpawning = false;

		world.spawnEntity(ent);
	}
}
