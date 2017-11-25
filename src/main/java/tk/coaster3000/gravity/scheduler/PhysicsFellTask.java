package tk.coaster3000.gravity.scheduler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import tk.coaster3000.gravity.event.BlockPhysicsEvent;
import tk.coaster3000.gravity.util.PhysicsUtil;

public class PhysicsFellTask extends PhysicsTask {
	public PhysicsFellTask(PhysicsScheduler scheduler, World world, BlockPos position) {
		super(scheduler, world, position);
	}

	@Override
	void execute() {
		IBlockState blockState = world.getBlockState(position);

		BlockPhysicsEvent.Fall bpfEvent = new BlockPhysicsEvent.Fall(world, position, blockState);

		MinecraftForge.EVENT_BUS.post(bpfEvent);

		if (!(bpfEvent.isCanceled() || bpfEvent.getResult().equals(Event.Result.DENY))) {
			PhysicsUtil.fellBlock(world, position, blockState);
		}
	}

	@Override
	public int compareTo(PhysicsTask o) {
		BlockPos oPosition = o.position;

		int diff = Integer.compare(position.getY(), oPosition.getY());

		if (diff != 0) return diff; // check y
		else diff = Integer.compare(position.getX(), oPosition.getY()); // go to X

		if (diff != 0) return diff; // check X
		else return Integer.compare(position.getZ(), oPosition.getZ()); //return Z
	}
}
