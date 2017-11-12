package tk.coaster3000.gravity;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static tk.coaster3000.gravity.util.PhysicsUtil.*;

public class BlockListener {

	public BlockListener(PhysicsScheduler scheduler) {
		this.scheduler = scheduler;
	}

	PhysicsScheduler scheduler;

	private boolean canFall(World world, BlockPos pos) {
		return canFall(world, pos, world.getBlockState(pos));
	}

	private boolean canFall(World world, BlockPos pos, IBlockState blockState) {
		return !(blockState.getBlock().getRegistryName() != null && blockState.getBlock().getRegistryName().toString().equals("minecraft:bedrock")) && (world.isAirBlock(pos.down()) || BlockFalling.canFallThrough(world.getBlockState(pos.down())));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onBlockPlace(BlockEvent.PlaceEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return; //Ignore the event
		BlockPos pos = event.getPos();
//		IBlockState state = event.getState();

		scheduler.scheduleTask(world, pos);
//		if (canFall(world, pos, state)) fellBlock(world, pos, state);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onBlockNeighborChange(BlockEvent.NeighborNotifyEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		BlockPos sPos = event.getPos(); // FIXME: Blocks near final layer of world will cause world to PLUMMET INTO DARKNESS
		for (EnumFacing face : event.getNotifiedSides()) { // Test horizontal
			BlockPos pos = sPos.offset(face);
			scheduler.scheduleTask(world, pos);
//			if (canFall(world, pos)) fellBlock(world, pos);

		}

	}

}
