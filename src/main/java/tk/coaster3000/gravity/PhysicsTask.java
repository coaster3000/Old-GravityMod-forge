package tk.coaster3000.gravity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import tk.coaster3000.gravity.event.BlockPhysicsEvent.Check;
import tk.coaster3000.gravity.event.BlockPhysicsEvent.Fall;
import tk.coaster3000.gravity.util.PhysicsUtil;


class PhysicsTask {
	private final World world;
	private final BlockPos position;

	public PhysicsTask(World world, BlockPos position) {
		this.world = world;
		this.position = position;
	}

	void execute() {
		// Block Physics Event
		IBlockState blockState = world.getBlockState(position);
		Check bpcEvent = new Check(world, position, blockState);

		MinecraftForge.EVENT_BUS.post(bpcEvent);

		if (!bpcEvent.isCanceled()) {
			boolean isFalling = PhysicsUtil.canBlockFall(world, position, blockState);
			if (isFalling) {
				Fall bpfEvent = new Fall(world, position, blockState);

				MinecraftForge.EVENT_BUS.post(bpfEvent);

				if (!(bpfEvent.isCanceled() || bpfEvent.getResult().equals(Event.Result.DENY))) {
					PhysicsUtil.fellBlock(world, position, blockState);
				}
			}
		}

	}
}
