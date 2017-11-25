package tk.coaster3000.gravity.scheduler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tk.coaster3000.gravity.event.BlockPhysicsEvent;
import tk.coaster3000.gravity.util.PhysicsUtil;

public class PhysicsCalculationTask extends PhysicsTask {

	public PhysicsCalculationTask(PhysicsScheduler scheduler, World world, BlockPos position) {
		super(scheduler, world, position);
	}

	@Override
	void execute() {
		IBlockState blockState = world.getBlockState(position);
		BlockPhysicsEvent.Check bpcEvent = new BlockPhysicsEvent.Check(world, position, blockState);

		MinecraftForge.EVENT_BUS.post(bpcEvent);

		if (!bpcEvent.isCanceled()) {
			boolean isFalling = PhysicsUtil.canBlockFall(world, position, blockState);
			if (isFalling) {
				scheduler.scheduleTask(new PhysicsFellTask(scheduler, world, position));
			}
		}
	}

	@Override
	public int compareTo(PhysicsTask o) {
		return 0;
	}
}
