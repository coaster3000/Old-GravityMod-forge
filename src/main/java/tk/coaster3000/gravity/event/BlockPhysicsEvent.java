package tk.coaster3000.gravity.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class BlockPhysicsEvent extends BlockEvent {
	BlockPhysicsEvent(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
	}

	/**
	 * Fired when physics checks are going to be done.
	 */
	public static class Check extends BlockPhysicsEvent {
		enum Result {
			DEFAULT,STATIC,FALL
		}

		public Result result = Result.DEFAULT;

		public Check(World world, BlockPos pos, IBlockState state) {
			super(world, pos, state);
		}

		@Override
		public boolean isCancelable() {
			return true;
		}
	}

	/**
	 * Fired when physics is applied to a block and it begins to fall.
	 */
	public static class Fall extends BlockPhysicsEvent {
		Fall(World world, BlockPos pos, IBlockState state) {
			super(world, pos, state);
		}
	}
}
