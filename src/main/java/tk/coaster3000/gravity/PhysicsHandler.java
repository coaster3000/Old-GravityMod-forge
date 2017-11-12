package tk.coaster3000.gravity;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tk.coaster3000.gravity.event.PhysicsTickEvent;
import tk.coaster3000.gravity.event.PhysicsTickEvent.Phase;

public class PhysicsHandler {

	private PhysicsScheduler physicsScheduler;

	public PhysicsHandler(PhysicsScheduler physicsScheduler) {
		this.physicsScheduler = physicsScheduler;
	}

	void onPhysicsTick(World world) {
		physicsScheduler.handleTick(world);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onEvent(TickEvent.WorldTickEvent event) {
		if (!physicsScheduler.hasWork(event.world)) return;

		PhysicsTickEvent tickEvent = new PhysicsTickEvent(Phase.START, event.world);
		MinecraftForge.EVENT_BUS.post(tickEvent);

		if (!tickEvent.isCanceled()) {
			onPhysicsTick(tickEvent.world);
			tickEvent = new PhysicsTickEvent(Phase.END, event.world);
			MinecraftForge.EVENT_BUS.post(tickEvent);
		}
	}
}
