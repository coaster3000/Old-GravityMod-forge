package tk.coaster3000.gravity;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldListener {

	private PhysicsScheduler physicsScheduler;

	public WorldListener(PhysicsScheduler physicsScheduler) {
		this.physicsScheduler = physicsScheduler;
	}

	@SubscribeEvent
	public void onEvent(WorldEvent.Load event) {
		if (event.getWorld().isRemote) return;
		physicsScheduler.addWorld(event.getWorld());
	}

	@SubscribeEvent
	public void onEvent(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) return;
		physicsScheduler.removeWorld(event.getWorld());
	}
}
