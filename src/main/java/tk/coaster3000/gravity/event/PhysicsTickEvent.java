package tk.coaster3000.gravity.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PhysicsTickEvent extends Event {
	public final Phase phase;
	public final World world;

	public enum Phase {
		START, END
	}

	public PhysicsTickEvent(Phase phase, World world) {
		this.phase = phase;
		this.world = world;
	}

	@Override
	public boolean isCancelable() {
		return phase == Phase.START;
	}
}
