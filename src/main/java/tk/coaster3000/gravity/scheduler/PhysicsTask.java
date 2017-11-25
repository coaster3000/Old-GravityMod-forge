package tk.coaster3000.gravity.scheduler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


abstract class PhysicsTask implements Comparable<PhysicsTask> {

	final World world;
	final BlockPos position;
	final PhysicsScheduler scheduler;

	public PhysicsTask(PhysicsScheduler scheduler, World world, BlockPos position) {
		this.scheduler = scheduler;
		this.world = world;
		this.position = position;
	}

	abstract void execute();
}
