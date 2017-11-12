package tk.coaster3000.gravity.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Set;

public class PhysicsUtil {


	public static void init() {

	}

	private static boolean isStringEqual(ResourceLocation resource, String string) {
		return string.equals((resource !=null? resource.toString():""));
	}

	/**
	 * Tells whether block is able to fall based on physics and whether it is allowed physics
	 * @see #canBlockFall(World, BlockPos, IBlockState)
	 * @see #isBlockAllowedPhysics(IBlockState)
	 * @param world
	 * @param pos
	 * @return true if block can fall, false otherwise
	 */
	public static boolean canBlockFall(World world, BlockPos pos) {
		return canBlockFall(world, pos, world.getBlockState(pos));
	}

	/**
	 * Tells whether block is able to fall based on physics and whether it is allowed physics
	 * @see #isBlockAllowedPhysics(IBlockState)
	 * @param world
	 * @param pos
	 * @param blockState
	 * @return true if block can fall, false otherwise
	 */
	public static boolean canBlockFall(World world, BlockPos pos, IBlockState blockState) {
		return isBlockAllowedPhysics(blockState) && (world.isAirBlock(pos.down()) || BlockFalling.canFallThrough(world.getBlockState(pos.down())));
	}

	/**
	 * Tells whether a block is allowed to fall.
	 * @param blockState block type
	 * @return true if block is not air or bedrock and is allowed to fall, false otherwise
	 */
	public static boolean isBlockAllowedPhysics(IBlockState blockState) {
		return blockState.getBlock() != Blocks.AIR && !isStringEqual(blockState.getBlock().getRegistryName(), "minecraft:bedrock");
	}

	/**
	 * Turns a block in specified world at specified position to fall using the current block state at time of call
	 * @param world
	 * @param position
	 */
	public static void fellBlock(World world, BlockPos position) {
		fellBlock(world, position, world.getBlockState(position));
	}

	/**
	 * Turns a block in specified world at specified position to fall with the specified blockState
	 * @param world
	 * @param position
	 * @param blockState
	 */
	public static void fellBlock(World world, BlockPos position, IBlockState blockState) {
		double x = position.getX() + 0.5, y = position.getY(), z = position.getZ() + 0.5;
		EntityFallingBlock ent = new EntityFallingBlock(world, x, y, z, blockState);
		ent.setOrigin(position.add(0.5,0,0.5));
		ent.preventEntitySpawning = false;

		world.spawnEntity(ent);
	}
}
